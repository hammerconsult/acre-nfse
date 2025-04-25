(function () {
    'use strict';

angular.module('nfseApp').controller('PrestadorServicosEditController',
    ['$scope', '$log', '$state', '$stateParams', '$modal', '$window', '$timeout', 'entity',
        'PrestadorServicos', 'Notificacao', 'Pessoa', 'Cnae', 'Servico', 'CEP', 'Municipio', 'SweetAlert', 'Configuracao', 'Upload', 'Blob',
        function ($scope, $log, $state, $stateParams, $modal, $window, $timeout, entity,
                  PrestadorServicos, Notificacao, Pessoa, Cnae, Servico, CEP, Municipio, SweetAlert, Configuracao, Upload, Blob) {

            $scope.prestadorServicos = entity;

            $scope.TIPO_PROCESSO = {
                PROCESSO_ADMINISTRATIVO: {descricao: "Processo Administrativo"},
                DECISAO_JUDICIAL: {descricao: "Decisão Judicial"}
            };

            $scope.TIPO_ISS = {
                HOMOLOGADO: {descricao: "Mensal (Homologado)"},
                FIXO: {descricao: "Anual (Fixo)"},
                ESTIMADO: {descricao: "Estimado"}
            };

            $scope.REGIME_TRIBUTACAO = {
                MICROEMPRESA_MUNICIPAL: {descricao: "Microempresa Municipal"},
                ESTIMATIVA: {descricao: "Estimativa"},
                SOCIEDADE_PROFISSIONAIS: {descricao: "Sociedade de Profissionais"},
                MICROEMPRESARIO_INDIVIDUAL: {descricao: "Microempresário Individual (MEI)"},
                MICROEMPRESÁRIO_EMPRESA_PEQUENO_PORTE: {descricao: "Microempresário e Empresa de Pequeno Porte (ME EPP)"}
            };

            $scope.loadByCpfCnpj = function () {
                PrestadorServicos.getPorCpfCnpj(
                    {cpfCnpj: $scope.prestadorServicos.pessoa.dadosPessoais.cpfCnpj}, function (result) {
                        $scope.editForm.cpfCnpj.$setValidity("exists", true);
                        if (result.pessoa) {
                            $scope.prestadorServicos = result;
                            Notificacao.error("Atenção", "Já existe um Prestador de Servicos com o CPF ou CNPJ informado," +
                                " você não pode solicitar o credenciamente de uma empresa já credenciada");
                            $scope.editForm.cpfCnpj.$setValidity("exists", false);
                        } else {
                            Pessoa.getPorCpfCnpj(
                                {cpfCnpj: $scope.prestadorServicos.pessoa.dadosPessoais.cpfCnpj}, function (result) {
                                    if (result.id) {
                                        $scope.prestadorServicos.pessoa = result;
                                    }
                                });
                        }
                    });
            };

            $scope.loadByCpfCnpjSocio = function (cpfCnpj, socio) {
                Pessoa.getPorCpfCnpj(
                    {cpfCnpj: cpfCnpj}, function (result) {
                        if (result.id) {
                            socio.pessoa = result;
                        }
                    });
            };

            var onSaveFinished = function (result) {
                $state.go("prestadorServicos");
                Notificacao.info("Prestador de Servicos salvo com sucesso")
            };


            $scope.save = function () {
                if ($scope.prestadorServicos.id != null) {
                    PrestadorServicos.update($scope.prestadorServicos, onSaveFinished);
                } else {
                    PrestadorServicos.save($scope.prestadorServicos, onSaveFinished);
                }
            };


            $scope.loadServicoByCodigo = function (codigo, index) {
                if (codigo) {
                    Servico.getPorCodigo(
                        {codigo: codigo}, function (result) {
                            if (result.id) {
                                $scope.prestadorServicos.servicos[index].servico = result;
                            } else {
                                Notificacao.warn("Atenção", "Não foi encotrado nenhum Servico com o código " + codigo);
                                $scope.prestadorServicos.servicos[index] = {};
                            }
                        });
                }
            };

            $scope.searchServico = function (index) {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/servico/servico-search.html',
                    controller: 'ServicoSearchController',
                    size: 'lg'
                });
                modalInstance.result.then(function (servico) {
                    $scope.prestadorServicos.servicos[index].servico = servico;
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.addSituacao = function () {
                if (!$scope.prestadorServicos.situacoesTributaria) {
                    $scope.prestadorServicos.situacoesTributaria = [];
                }
                $scope.definirAtributoVigenteParaSituacao($scope.situacao);
                var nenhumaVigente = true;
                angular.forEach($scope.prestadorServicos.situacoesTributaria, function (situacao) {
                    $scope.definirAtributoVigenteParaSituacao(situacao);
                    if (situacao.vigente) {
                        SweetAlert.swal("Operação não permitida!", "Já existe uma situação vigente, encerre as vigências de todas as situações para inserir uma nova!", "warning");
                        nenhumaVigente = false;
                    }
                });
                if (nenhumaVigente == true) {
                    $scope.prestadorServicos.situacoesTributaria.push($scope.situacao);
                    $scope.mostraSituacao = false;
                    $scope.situacao = {};
                }
            };

            $scope.addIsencao = function () {
                if (!$scope.prestadorServicos.isencoes) {
                    $scope.prestadorServicos.isencoes = [];
                }
                $scope.definirAtributoVigenteParaSituacao($scope.isencao);
                var nenhumaVigente = true;
                angular.forEach($scope.prestadorServicos.isencoes, function (isencao) {
                    $scope.definirAtributoVigenteParaSituacao(isencao);
                    if (isencao.vigente && isencao.servico.id == $scope.isencao.servico.id) {
                        SweetAlert.swal("Operação não permitida!", "Já existe uma isênção vigente, encerre as vigências de todas as isenções para inserir uma nova!", "warning");
                        nenhumaVigente = false;
                    }
                });
                if (nenhumaVigente == true) {
                    $scope.prestadorServicos.isencoes.push($scope.isencao);
                    $scope.mostraIsencao = false;
                    $scope.isencao = {};
                }
            };

            $scope.addSuspensao = function () {
                if (!$scope.prestadorServicos.suspensoes) {
                    $scope.prestadorServicos.suspensoes = [];
                }
                $scope.definirAtributoVigenteParaSituacao($scope.suspensao);
                var nenhumaVigente = true;
                angular.forEach($scope.prestadorServicos.suspensoes, function (suspensao) {
                    $scope.definirAtributoVigenteParaSituacao(suspensao);
                    if (suspensao.vigente && suspensao.servico.id == $scope.suspensao.servico.id) {
                        SweetAlert.swal("Operação não permitida!", "Já existe uma suspensão vigente, encerre as vigências de todas as suspensões para inserir uma nova!", "warning");
                        nenhumaVigente = false;
                    }
                });
                if (nenhumaVigente == true) {
                    $scope.prestadorServicos.suspensoes.push($scope.suspensao);
                    $scope.mostraSuspensao = false;
                    $scope.suspensao = {};
                }
            };

            $scope.limparSituacao = function () {
                $scope.situacao = {};
                if ($scope.prestadorServicos.situacoesTributaria.length > 0) {
                    $scope.mostraSituacao = false;
                }
            };

            $scope.limparIsencao = function () {
                $scope.isencao = {};
                $scope.mostraIsencao = false;
            };

            $scope.limparSuspensao = function () {
                $scope.suspensao = {};
                $scope.mostraSuspensao = false;
            };


            $scope.removeSituacao = function (index) {
                $scope.prestadorServicos.situacoesTributaria.splice(index, 1);
                if ($scope.prestadorServicos.situacoesTributaria.length == 0) {
                    $scope.mostraSituacao = true;
                }
            };

            $scope.removeIsencao = function (index) {
                $scope.prestadorServicos.isencoes.splice(index, 1);
            };

            $scope.removeSuspensao = function (index) {
                $scope.prestadorServicos.suspensoes.splice(index, 1);
            };

            $scope.editarSituacao = function (index) {
                $scope.situacao = $scope.prestadorServicos.situacoesTributaria[index];
                $scope.removeSituacao(index);
                $scope.mostraSituacao = true;
            };

            $scope.editarIsencao = function (index) {
                $scope.isencao = $scope.prestadorServicos.isencoes[index];
                $scope.removeIsencao(index);
                $scope.mostraIsencao = true;
            };

            $scope.editarSuspensao = function (index) {
                $scope.suspensao = $scope.prestadorServicos.suspensoes[index];
                $scope.removeSuspensao(index);
                $scope.mostraSuspensao = true;
            };

            $scope.removeServico = function (index) {
                $scope.prestadorServicos.servicos.splice(index, 1);
                if ($scope.prestadorServicos.servicos.length == 0) {
                    $scope.addServico();
                }
            };

            $scope.removeSocio = function (index) {
                $scope.prestadorServicos.socios.splice(index, 1);
                if ($scope.prestadorServicos.socios.length == 0) {
                    $scope.addSocio();
                }
            };

            $scope.addServico = function () {
                if (!$scope.prestadorServicos.servicos) {
                    $scope.prestadorServicos.servicos = [];
                }
                $scope.prestadorServicos.servicos.push({servico: {}});
            };

            $scope.addSocio = function () {
                if (!$scope.prestadorServicos.socios) {
                    $scope.prestadorServicos.socios = [];
                }
                $scope.prestadorServicos.socios.push({pessoa: {id: null}});
            };

            $scope.addUsuario = function () {
                if (!$scope.prestadorServicos.usuarios) {
                    $scope.prestadorServicos.usuarios = [];
                }
                $scope.prestadorServicos.usuarios.push({usuario: {}});
            };
            $scope.removeUsuario = function (index) {
                $scope.prestadorServicos.usuarios.splice(index, 1);
            };

            $scope.loadEnderecoByCEP = function (cep) {
                if (cep) {
                    CEP.getByCep({cep: cep}, function (endereco) {
                        if (endereco.municipio) {
                            $scope.prestadorServicos.pessoa.dadosPessoais.endereco.cep = endereco.cep;
                            $scope.prestadorServicos.pessoa.dadosPessoais.endereco.municipio = endereco.municipio;
                            $scope.prestadorServicos.pessoa.dadosPessoais.endereco.logradouro = endereco.logradouro;
                            $scope.prestadorServicos.pessoa.dadosPessoais.endereco.bairro = endereco.bairro;
                        }
                    });
                }
            };

            $scope.loadMunicipioByCodigo = function (codigo) {
                if (codigo) {
                    Municipio.getByCodigo({codigo: codigo}, function (municipio) {
                        if (municipio.id) {
                            $scope.prestadorServicos.pessoa.dadosPessoais.endereco.municipio = municipio;
                        } else {
                            Notificacao.warn("Atenção", "Não foi encotrado nenhum Municipio com o código " + codigo);
                            $scope.prestadorServicos.pessoa.dadosPessoais.endereco.municipio = {};
                        }
                    });
                }
            };

            $scope.searchMunicipio = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/municipio/municipio-search.html',
                    controller: 'MunicipioSearchController',
                    size: 'lg'
                });
                modalInstance.result.then(function (municipio) {
                    $scope.prestadorServicos.pessoa.dadosPessoais.endereco.municipio = municipio;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.definirAtributoVigenteParaSituacao = function (vigencia) {
                var hoje = new Date();
                vigencia.vigente = false;
                if (vigencia.inicioVigencia) {
                    var inicio = new Date(vigencia.inicioVigencia);
                    var final;
                    if (vigencia.finalVigencia) {
                        var final = new Date(vigencia.finalVigencia);
                    }
                    if (inicio <= hoje.getTime()
                        && (!final || final >= hoje.getTime())) {
                        vigencia.vigente = true;
                    }
                }
            };

            $scope.isFormValid = function () {
                if ($scope.prestadorServicos &&
                    $scope.prestadorServicos.situacoesTributaria && $scope.prestadorServicos.situacoesTributaria > 0
                    && $scope.editForm.$valid) {
                    return true;
                } else {
                    return false;
                }
            };

            $scope.searchUser = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/admin/user-management/user-management-search.html',
                    controller: 'UserManagementSearchController',
                    size: 'lg'
                });
                modalInstance.result.then(function (user) {
                    var usuarioEmpresa = {usuario: user}
                    if (!$scope.prestadorServicos.usuarios) {
                        $scope.prestadorServicos.usuarios = [];
                    }
                    $scope.prestadorServicos.usuarios.push(usuarioEmpresa);
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            if (!$scope.prestadorServicos.usuarios || $scope.prestadorServicos.usuarios == 0) {
                //$scope.addUsuario();
            }
            if (!$scope.prestadorServicos.isencoes) {
                $scope.prestadorServicos.isencoes = [];
            }
            if (!$scope.prestadorServicos.suspensoes) {
                $scope.prestadorServicos.suspensoes = [];
            }
            if (!$scope.prestadorServicos.situacoesTributaria || $scope.prestadorServicos.situacoesTributaria == 0) {
                $scope.prestadorServicos.situacoesTributaria = [];
                $scope.mostraSituacao = true;
            } else {
                angular.forEach($scope.prestadorServicos.situacoesTributaria, function (situacao) {
                    $scope.definirAtributoVigenteParaSituacao(situacao);
                });
            }
            if (!$scope.prestadorServicos.servicos || $scope.prestadorServicos.servicos.length == 0) {
                $scope.addServico();
            }

            if (!$scope.prestadorServicos.socios || $scope.prestadorServicos.socios.length == 0) {
                $scope.addSocio();
            }
            if (!$scope.prestadorServicos.documentosApresentados || $scope.prestadorServicos.documentosApresentados.length == 0) {
                $scope.prestadorServicos.documentosApresentados = [];
                Configuracao.get(function (data) {
                    for (var i = 0; i < data.documentosObrigatorios.length; i++) {
                        $scope.prestadorServicos.documentosApresentados.push({
                            apresentado: false,
                            documentoObrigatorio: data.documentosObrigatorios[i]
                        });
                    }
                });
            }

            $scope.situacao = {};
            $scope.isencao = {};
            $scope.suspensao = {};

            $scope.mostraFormDocumento = false;
            $scope.documentoApresentado = {};

            $scope.adicionarDocumento = function () {
                $scope.prestadorServicos.documentosApresentados.push($scope.documentoApresentado);
                $scope.documentoApresentado = {};
                $scope.mostraFormDocumento = false;
            };

            $scope.limparDocumento = function () {
                $scope.mostraFormDocumento = false;
                $scope.documentoApresentado = {};
            };

            $scope.removerDocumento = function (index) {
                $scope.prestadorServicos.documentosApresentados.splice(index, 1);
            };

            function getBlobFromBase64(arquivo) {
                /*data:{{vm.arquivo.contentType}};base64,{{vm.arquivo.bytes}}*/
                var byteCharacters = atob(arquivo.conteudo);
                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var byteArray = new Uint8Array(byteNumbers);
                var blob = new Blob([byteArray], {type: arquivo.contentType});
                return blob;
            }

            $scope.baixarArquivo = function (index) {
                var documentoApresentado = $scope.prestadorServicos.documentosApresentados[index];
                if (documentoApresentado.arquivo) {
                    var fileName = documentoApresentado.arquivo.nome;
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.style = "display: none";

                    var file = getBlobFromBase64(documentoApresentado.arquivo);
                    var fileURL = window.URL.createObjectURL(file);
                    a.href = fileURL;
                    a.download = fileName;
                    a.click();
                }
            };

            $scope.removerArquivo = function (index) {
                var documentoApresentado = $scope.prestadorServicos.documentosApresentados[index];
                if (documentoApresentado.arquivo) {
                    documentoApresentado.arquivo = null;
                }
            };

            $scope.novoDocumento = function () {
                $scope.mostraFormDocumento = true;
                $scope.documentoApresentado = {};
                $scope.documentoApresentado.apresentado = true;
                $timeout(function () {
                    angular.element('[ng-model="documentoApresentado.descricao"]').focus();
                });
            };

            $scope.upload = function (file) {
                if (file) {
                    var modalInstance = $modal.open({
                        animation: true,
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: 'myModalContent.html',
                        controller: 'ModalInstanceCtrl',
                        controllerAs: '$ctrl',
                        resolve: {
                            documentosApresentados: function () {
                                return $scope.prestadorServicos.documentosApresentados;
                            },
                            file: function () {
                                return file;
                            }
                        }
                    });
                    modalInstance.result.then(function (documentoApresentado) {
                        Upload.base64DataUrl(documentoApresentado.arquivo).then(function (url) {
                                documentoApresentado.arquivo.nome = documentoApresentado.arquivo.name;
                                documentoApresentado.arquivo.contentType = documentoApresentado.arquivo.type;
                                var arquivo = {};
                                var arr = url.split(',');
                                var base64 = arr[arr.length - 1];
                                arquivo = base64;
                                documentoApresentado.arquivo.conteudo = arquivo;
                            }, function (resp) {
                            }, function (evt) {
                            }
                        );
                        //$log.info(documentoApresentado);
                    }, function () {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                    file = null;
                }
            };

        }]);

angular.module('nfseApp').
    controller('ModalInstanceCtrl', function ($modalInstance, documentosApresentados, file) {
        var $ctrl = this;
        $ctrl.documentosApresentados = documentosApresentados;

        $ctrl.ok = function (documentoApresentado) {
            documentoApresentado.arquivo = file;
            documentoApresentado.apresentado = true;
            $modalInstance.close(documentoApresentado);
        };

        $ctrl.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

})();