(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('NavbarController',
            function ($scope, $rootScope, $location, $state, $window, Auth, Principal, localStorageService,
                      Imagem, NotaFiscal, DeclaracaoMensalServico, TrocaEmpresa, Configuracao, MensagemContribuinteUsuarioService,
                      SweetAlert, Sistema, PrestadorServicos) {
                $scope.$state = $state;
                $scope.msgNaoLidas = 0;
                $scope.versaoSistema;

                $rootScope.$on('nfseApp:navbarUpdate', function (event, account) {
                    $scope.init();
                });

                $scope.init = function () {
                    Sistema.getVersaoSistema({}, function (data) {
                        $scope.versaoSistema = data.versao;
                    });

                    Configuracao.getPerfil({}, function (data) {
                        $scope.perfilApp = data.perfil;
                    });

                    Principal.identity().then(function (account) {
                        if (account) {
                            $scope.account = account;
                            $scope.isAuthenticated = Principal.isAuthenticated;
                            $scope.carregarImagemUsuario();
                            $scope.carregarDadosDoPrestador();
                        }
                    });

                    Principal.config().then(function (configuracao) {
                        $rootScope.configuracao = configuracao;
                        $rootScope.localizacao = configuracao.municipio;
                        $rootScope.logoMunicipio = configuracao.logoMunicipio;
                        $scope.bloqueiaLogin = configuracao.bloqueiaLogin;
                        $scope.mensagemLogin = configuracao.mensagemLogin;
                        $scope.permiteNfsAvulsa = configuracao.permiteNfsAvulsa;

                        $scope.lancarComunicado();
                    });
                }

                $scope.init();

                $scope.countMensagensNaoLidas = function () {
                    MensagemContribuinteUsuarioService.countNaoLidaNoPrazo({login: $scope.account.login}, function (data) {
                        $scope.msgNaoLidas = data.value;
                    });
                };

                $scope.hasRole = function (role) {
                    return Principal.isInRole(role);
                }

                $scope.limparComunicado = function () {
                    localStorageService.set("mensagemLoginLida", null);
                    $scope.lancarComunicado();
                }

                $scope.lancarComunicado = function () {
                    if ($scope.mensagemLogin && !localStorageService.get("mensagemLoginLida")) {
                        SweetAlert.swal({
                                animation: false,
                                title: "Informação",
                                text: $scope.mensagemLogin,
                                html: true,
                                type: 'warning',
                                showCancelButton: false,
                                closeOnConfirm: true,
                                closeOnCancel: true
                            },
                            function (isConfirm) {
                                if (isConfirm) {
                                    localStorageService.set("mensagemLoginLida", true);
                                    SweetAlert.close();
                                }
                            });
                    }
                }

                $scope.logout = function () {
                    Auth.logout();
                    $scope.account = null;
                    $scope.prestadorPrincipal = null;
                    $rootScope.logoPrestador = null;
                    localStorageService.set("prestadorPrincipal", null);
                    $state.go('login');
                }

                $scope.carregarImagemUsuario = function () {
                    if ($scope.account) {
                        Imagem.getImageFromUsuario({}, function (imagem) {
                            if (imagem && imagem.conteudo) {
                                $scope.account.fotoAjustada = imagem.conteudo;
                            }
                        }, function () {
                            if ($scope.account) {
                                $scope.account.fotoAjustada = "/content/images/user-default.jpg";
                            }
                        })
                    }
                }

                $scope.carregarImagemPrestador = function () {
                    if ($scope.prestadorPrincipal && $scope.prestadorPrincipal.prestador) {
                        Imagem.getImageFromPrestador({id: $scope.prestadorPrincipal.prestador.id}, function (result) {
                            $rootScope.logoPrestador = result
                        }, function (error) {
                            $rootScope.logoPrestador = null;
                        });
                    } else {
                        $rootScope.logoPrestador = null;
                    }
                }

                $scope.carregarDadosDoPrestador = function () {
                    $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
                    if ($scope.prestadorPrincipal) {
                        PrestadorServicos.get({id: $scope.prestadorPrincipal.prestador.id}, function (data) {
                            $scope.prestadorPrincipal.prestador = data;
                            $scope.carregarImagemPrestador();
                            $scope.countMensagensNaoLidas();

                        });
                    }
                }

                $scope.abrirTrocarEmpresa = function () {
                    TrocaEmpresa.open();
                }

                $scope.nomePrestador = function () {
                    if ($scope.prestadorPrincipal &&
                        $scope.prestadorPrincipal.prestador &&
                        $scope.prestadorPrincipal.prestador.id) {
                        var nomeRazaoSocial = $scope.prestadorPrincipal.prestador.inscricaoMunicipal + ' - ' + $scope.prestadorPrincipal.prestador.pessoa.dadosPessoais.nomeRazaoSocial;
                        return nomeRazaoSocial.length <= 50 ?
                            nomeRazaoSocial :
                            nomeRazaoSocial.substring(0, 47) + '...';
                    }
                }

                $scope.isPrestadorPermitido = function () {
                    return $scope.prestadorPrincipal && $scope.prestadorPrincipal.permitido;
                }

                $scope.isInstituicaoFinanceira = function () {
                    return $scope.prestadorPrincipal &&
                        $scope.prestadorPrincipal.permitido &&
                        $scope.prestadorPrincipal.prestador &&
                        $scope.prestadorPrincipal.prestador.enquadramentoFiscal &&
                        $scope.prestadorPrincipal.prestador.enquadramentoFiscal.instituicaoFinanceira;
                }

                $scope.isVersaoDesifAbrasf32 = function () {
                    return $scope.prestadorPrincipal &&
                        $scope.prestadorPrincipal.prestador &&
                        $scope.prestadorPrincipal.prestador.enquadramentoFiscal &&
                        $scope.prestadorPrincipal.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_ABRASF_3_2';
                }
            })
    ;
})();
