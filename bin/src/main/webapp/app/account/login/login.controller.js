(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth, Account,
                                                 PrestadorServicos, Principal, Idle, SweetAlert, User,
                                                 Notificacao, Configuracao, Noticia, NotaFiscal,
                                                 MensagemContribuinteUsuarioService, localStorageService,
                                                 ConfiguracaoGovBrService) {
            $scope.user = {};
            $scope.errors = {};
            $scope.recaptcha = null;
            $scope.rememberMe = true;
            $scope.bloqueiaLogin = false;
            $scope.mensagemLogin = null;
            $scope.ultimasNoticias = [];
            $scope.primeiraNoticia = null;
            $scope.outrasNoticias = [];
            $scope.placar = null;
            $scope.tamanhoImagem = 200;
            $scope.carregando = false;
            $scope.configuracao;
            $scope.configuracaoGovBr;


            $timeout(function () {
                angular.element('[ng-model="username"]').focus();
                Configuracao.get({}, function (configuracao) {
                    $scope.configuracao = configuracao;
                });
            });

            ConfiguracaoGovBrService.getConfiguracaoGovBr({}, function (data) {
                $scope.configuracaoGovBr = data;
            });

            //manda p o home caso tente acessar o login e já está logado
            Principal.identity().then(function (account) {
                if (account) {
                    //manda p o home caso tente acessar o login e já está logado
                    $state.go('home', {reload: true});
                }
            });

            $scope.buscarUltimasNoticias = function () {
                Noticia.buscarUltimasNoticias({}, function (data) {
                    $scope.ultimasNoticias = data;
                    if ($scope.ultimasNoticias && $scope.ultimasNoticias.length > 0) {
                        $scope.primeiraNoticia = $scope.ultimasNoticias[0];
                        for (var i = 1; i < $scope.ultimasNoticias.length; i++) {
                            $scope.outrasNoticias.push($scope.ultimasNoticias[i]);
                        }
                    }
                    definirTamanhoDasImagens();
                }, function () {
                    definirTamanhoDasImagens()
                });
            }


            function definirTamanhoDasImagens() {
                $timeout(function () {
                    $scope.heigthAcessoSistema = $('#acesso-sistema').height();
                    for (var x = 0; x < $scope.ultimasNoticias.length; x++) {
                        var noticia = $scope.ultimasNoticias[x];
                        var idImagem = '#imagem-noticia-' + noticia.id;
                        $(idImagem).height($scope.heigthAcessoSistema);
                        $(idImagem).width('100%');
                        $(idImagem).css('background', '#fff url(' + noticia.imagem + ') no-repeat');
                        $(idImagem).css('background-size', '100% ' + $scope.heigthAcessoSistema + 'px');
                    }
                    $('#ibox-emissores').height(($scope.heigthAcessoSistema / 2) - 16);
                    $('#ibox-emissores').css('background-color', 'white');
                    $('#ibox-notas').height(($scope.heigthAcessoSistema / 2) - 16);
                    $('#ibox-notas').css('background-color', 'white');
                    $scope.carregando = false;
                });
            }

            $scope.buscarPlacar = function () {
                $scope.carregando = true;
                NotaFiscal.buscarPlacar({}, function (data) {
                    $scope.placar = data;
                    $scope.buscarUltimasNoticias();
                }, function () {
                    $scope.buscarUltimasNoticias();
                });
            }

            $scope.login = function (event) {
                if ($scope.configuracao && $scope.configuracao.bloqueiaLogin) {
                    Notificacao.warn("Atenção", "Serviço temporariamente desabilitado.");
                    return;
                }
                event.preventDefault();
                Auth.login({
                    username: $scope.username,
                    password: $scope.password,
                    rememberMe: $scope.rememberMe
                }).then(function () {
                    $scope.posLogin();
                }).catch(function (error) {
                    var msg = [];
                    if (!$scope.username) {
                        msg.push("Informe o CPF ou CNPJ");
                    }
                    if (!$scope.password) {
                        msg.push("Informe a senha");
                    }
                    if (error.data && error.data.error_description && error.data.error_description === 'UserNotActivated') {
                        msg.push(" Seu cadastro ainda não foi ativado. Por favor, verifique se recebeu um e-mail contendo instruções de ativação do cadastro. Caso contrário utilize a opção 'Esqueceu a senha?' para reenviar o e-mail de ativação do cadastro. Se mesmo assim não receber o e-mail verifique na sua caixa de Spam");
                    }
                    if (msg.length == 0) {
                        User.acrescentarTentativaAcesso({login: $scope.username});
                        msg.push("Usuário ou senha incorreto! Tente novamente ou clique em \"Esqueceu a senha\" para a redefinir.");
                    }
                    Notificacao.error("Atenção", msg);
                });
            };

            $scope.posLogin = function () {
                $scope.authenticationError = false;
                $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
                if (!Principal.isInRole('ROLE_ADMIN') &&
                    $scope.prestadorPrincipal &&
                    $scope.prestadorPrincipal.prestador &&
                    $scope.prestadorPrincipal.prestador.id) {
                    MensagemContribuinteUsuarioService.proximaMensagemNaoLida({}, function (data) {
                        if (data && data.id) {
                            $state.go("mensagemContribuinteUsuarioNew", {id: data.id});
                        } else {
                            $scope.redirecionar();
                        }
                    });
                } else {
                    $scope.redirecionar();
                }
                $scope.$emit('nfseApp:navbarUpdate', event);
                // start watching when the app runs. also starts the Keepalive service by default.
                Idle.watch();
                $rootScope.$on('IdleStart', function () {
                    SweetAlert.swal({
                            title: "Cadê você?",
                            text: "Já faz algum tempo que você não trabalha na aplicação, logo sua sessão irá expirar e será redirecionado para a tela de login",
                            type: "warning",
                            showCancelButton: false,
                            confirmButtonText: "Voltar!",
                            closeOnConfirm: true
                        },
                        function () {
                            Account.get();
                        });
                    // the user appears to have gone idle
                });
                $rootScope.$on('IdleTimeout', function () {
                    SweetAlert.swal("OPS!", "Sua sessão expirou, terá que fazer login novamente", "warning");
                    Auth.logout();
                    $state.go('login');
                    // the user has timed out (meaning idleDuration + timeout has passed without any activity)
                    // this is where you'd log them
                });
            }

            $scope.redirecionar = function () {
                console.log('redirecionar {}', $scope.prestadorPrincipal);
                User.zerarTentativaAcesso({login: $scope.prestadorPrincipal.login});
                if ($scope.prestadorPrincipal.passwordTemporary) {
                    $state.go('primeiroAcesso');
                } else {
                    $state.go('home', {reload: true});
                    setTimeout(function () {
                        $rootScope.$broadcast("nfseApp:verificarDebitos");
                    }, 1000);
                }
            }

            $scope.primeiroAcesso = function () {
                if ($scope.configuracao.habilitaPrimeiroAcesso) {
                    $state.go("register");
                } else {
                    Notificacao.info("Informação", configuracao.mensagemPrimeiroAcessoDesabilitado);
                }
            }

            $scope.buscarPlacar();
        });
})();
