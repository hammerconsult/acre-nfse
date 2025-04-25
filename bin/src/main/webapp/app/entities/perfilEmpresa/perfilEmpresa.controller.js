(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PerfilEmpresaController',
        function ($scope, $rootScope, $stateParams, PrestadorServicos, localStorageService, Principal, $log,
                  Imagem, Notificacao, SweetAlert, $modal, $state, EnumeradoJsonService, Configuracao) {

            $scope.rolesEmpresa = EnumeradoJsonService.getEnumValues("roles")
            $scope.configuracao;

            $scope.load = function () {
                var prestador = localStorageService.get("prestadorPrincipal").prestador;
                PrestadorServicos.get({id: prestador.id}, function (result) {
                    $scope.prestadorServicos = result;
                    $scope.prestadorServicos.usuarios = [];
                    Imagem.getImageFromPessoa({id: $scope.prestadorServicos.pessoa.id}, function (imagem) {
                        if (imagem && imagem.conteudo) {
                            console.log(imagem);
                            $scope.imagemPrestador = imagem.conteudo;
                        }
                    });

                    PrestadorServicos.getUsuariosPrestador({}, function (data) {
                        $scope.prestadorServicos.usuarios = data;
                    });

                    PrestadorServicos.getTributosFederais({prestadorId: $scope.prestadorServicos.id},
                        function (result) {
                            $scope.prestadorServicos.tributosFederais = result;
                        });

                });
                Configuracao.get({}, function (data) {
                    $scope.configuracao = data;
                });
            };

            $scope.load();

            $scope.trocarChaveAcesso = function () {
                PrestadorServicos.trocarChaveAcesso({id: $scope.prestadorServicos.id}, function (result) {
                    $scope.load();
                });
            };


            $scope.atualizarPrestadorUsuario = function (user) {
                PrestadorServicos.atualizarPrestadorUsuario(user, function () {
                    $scope.load();
                });
            };

            $scope.ativarOuDesativarUsuario = function (user) {
                SweetAlert.swal({
                        title: "Confirme a alteração de situação do usuário",
                        text: "Você tem certeza que quer " + (user.activated ? "Desativar" : "Ativar") + " esse usuário?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Sim",
                        cancelButtonText: "Não, Cancelar",
                        closeOnConfirm: false,
                        closeOnCancel: false
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            PrestadorServicos.ativarOuDesativarUsuarioEmpresa({login: user.login}, function () {
                                Notificacao.info("Informação", "Usuário alterado com sucesso.");
                                $scope.load();
                            });
                        } else {
                            SweetAlert.swal("Cancelado", "O usuário não foi removido", "error");
                        }
                    });
            };

            $scope.searchUser = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/admin/user-management/user-management-search-by-cpf.html',
                    controller: 'UserManagementSearchCpfController',
                    size: 'lg'
                });
                modalInstance.result.then(function (user) {
                    if (hasUsuarioAdicionado(user)) {
                        Notificacao.warn('Atenção', 'Usuário já adicionado para essa empresa.')
                    } else {
                        PrestadorServicos.vincularNovoUsuarioEmpresa({login: user.login}, function () {
                            Notificacao.info("Informação", "Usuário vinculado com sucesso.");
                            $scope.load();
                        });

                    }

                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            function hasUsuarioAdicionado(user) {
                if ($scope.prestadorServicos.usuarios) {
                    for (var i = 0; i < $scope.prestadorServicos.usuarios.length; i++) {
                        if ($scope.prestadorServicos.usuarios[i].login == user.login) {
                            return true;
                        }
                    }
                }
                return false;
            }

            $scope.getNomeEscritorio = function () {
                var nome = "";
                if ($scope.prestadorServicos
                    && $scope.prestadorServicos.enquadramentoFiscal
                    && $scope.prestadorServicos.enquadramentoFiscal.escritorioContabil) {
                    if ($scope.prestadorServicos.enquadramentoFiscal.escritorioContabil.dadosPessoais) {
                        nome = $scope.prestadorServicos.enquadramentoFiscal.escritorioContabil.dadosPessoais.nomeRazaoSocial;
                        nome += "(" + $scope.prestadorServicos.enquadramentoFiscal.escritorioContabil.dadosPessoais.cpfCnpj + ")";
                    } else if ($scope.prestadorServicos.enquadramentoFiscal.escritorioContabil.responsavel) {
                        nome = $scope.prestadorServicos.enquadramentoFiscal.escritorioContabil.responsavel.nomeRazaoSocial;
                        nome += "(" + $scope.prestadorServicos.enquadramentoFiscal.escritorioContabil.responsavel.cpfCnpj + ")";
                    }
                }
                return nome;
            }

            $scope.getEnderecoCompleto = function () {
                var endereco = "";
                if ($scope.prestadorServicos && $scope.prestadorServicos.pessoa && $scope.prestadorServicos.pessoa.dadosPessoais) {
                    if ($scope.prestadorServicos.pessoa.dadosPessoais.logradouro) {
                        endereco += $scope.prestadorServicos.pessoa.dadosPessoais.logradouro;
                    }
                    endereco = adicionarVirgulaAoEndereco(endereco);
                    if ($scope.prestadorServicos.pessoa.dadosPessoais.numero) {
                        endereco += $scope.prestadorServicos.pessoa.dadosPessoais.numero;
                    }
                    endereco = adicionarVirgulaAoEndereco(endereco);
                    if ($scope.prestadorServicos.pessoa.dadosPessoais.complemento) {
                        endereco += $scope.prestadorServicos.pessoa.dadosPessoais.complemento;
                    }
                    endereco = adicionarVirgulaAoEndereco(endereco);
                    if ($scope.prestadorServicos.pessoa.dadosPessoais.bairro) {
                        endereco += $scope.prestadorServicos.pessoa.dadosPessoais.bairro;
                    }
                    endereco = adicionarVirgulaAoEndereco(endereco);
                    if ($scope.prestadorServicos.pessoa.dadosPessoais.cep) {
                        endereco += 'Cep: ' + $scope.prestadorServicos.pessoa.dadosPessoais.cep;
                    }
                    endereco = adicionarVirgulaAoEndereco(endereco);
                    if ($scope.prestadorServicos.pessoa.dadosPessoais.municipio) {
                        endereco += $scope.prestadorServicos.pessoa.dadosPessoais.municipio.nome;
                    }
                    endereco = adicionarVirgulaAoEndereco(endereco);
                    if ($scope.prestadorServicos.pessoa.dadosPessoais.municipio) {
                        endereco += $scope.prestadorServicos.pessoa.dadosPessoais.municipio.estado;
                    }
                }
                return endereco;
            };

            function adicionarVirgulaAoEndereco(endereco) {
                if (endereco !== null && endereco !== '' && !endereco.trim().endsWith(",")) {
                    endereco = endereco.trim() + ", ";
                }
                return endereco;
            }

            $scope.trocarImagemEmpresa = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/perfilEmpresa/uploadImagemEmpresa.html',
                    controller: 'UploadImagemEmpresaController',
                    size: 'lg',
                    resolve: {
                        imagemPrestador: function () {
                            return $scope.imagemPrestador;
                        },
                        prestadorServicos: function () {
                            return $scope.prestadorServicos;
                        }
                    }
                });
            };

            $scope.searchEscritorioContabil = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/escritorioContabil/escritorioContabil-search.html',
                    controller: 'EscritorioContabilSearchController',
                    size: 'lg'
                });
                modalInstance.result.then(function (result) {
                    $scope.prestadorServicos.enquadramentoFiscal.escritorioContabil = result;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.save = function () {
                PrestadorServicos.update($scope.prestadorServicos, function (result) {
                    $scope.$emit('nfseApp:prestadorServicosUpdate', result);
                    $state.reload();
                    Notificacao.info("Perfil Salvo com sucesso.");
                });
            };

            $scope.ajustaNomeEnum = function (nome) {
                if (!nome) {
                    return "";
                }
                try {
                    nome = nome.charAt(0).toUpperCase() + nome.slice(1).toLowerCase();
                    return nome.replace(/_/g, ' ');
                } catch (e) {
                    return "";
                }
            }

            $scope.salvarTributosFederais = function () {
                PrestadorServicos.salvarTributosFederais($scope.prestadorServicos.tributosFederais, function () {
                    $scope.load();
                });
            };

        });
})();