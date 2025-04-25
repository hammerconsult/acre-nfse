(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ConfiguracaoDetailController', function ($modal, $state, $scope, $rootScope, $stateParams, entity, Configuracao, Municipio, Notificacao) {
        $scope.configuracao = entity;
        $scope.documentoObrigatorio = getNovoDocumentoObrigatorio();

        $scope.load = function (id) {
            Configuracao.get({id: id}, function (result) {
                $scope.configuracao = result;
            });
        };
        $rootScope.$on('nfseApp:configuracaoUpdate', function (event, result) {
            $scope.configuracao = result;
        });

        var onSaveFinished = function (result) {
            $state.go("configuracao");
            Notificacao.info("Configuração do Sistema salva com sucesso!")
        };

        $scope.searchMunicipio = function (caraQueTemMunicipio) {
            var modalInstance = $modal.open({
                templateUrl: 'app/entities/municipio/municipio-search.html',
                controller: 'MunicipioSearchController',
                size: 'lg'
            });
            modalInstance.result.then(function (municipio) {
                caraQueTemMunicipio.municipio = municipio;
                caraQueTemMunicipio.pais = {};
            }, function () {
                //$log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.loadMunicipioByCodigo = function (codigo, caraQueTemMunicipio) {
            if (codigo) {
                Municipio.getByCodigo({codigo: codigo}, function (municipio) {
                    if (municipio.id) {
                        caraQueTemMunicipio.municipio = municipio;
                        caraQueTemMunicipio.pais = {};
                    } else {
                        Notificacao.warn("Atenção", "Não foi encotrado nenhum Municipio com o código " + codigo);
                        caraQueTemMunicipio.municipio = {};
                    }
                });
            }
        };

        $scope.save = function () {
            if ($scope.configuracao.documentosObrigatorios) {
                for (var i = 0; i < $scope.configuracao.documentosObrigatorios.length; i++) {
                    if (!$scope.configuracao.documentosObrigatorios[i].descricao) {
                        Notificacao.warn('Operação não permitida!', 'É obrigatório informar a descrição de todos documentos adicionados!');
                        return;
                    }
                }
                for (var i = 0; i < $scope.configuracao.documentosObrigatorios.length; i++) {
                    for (var x = 0; i < $scope.configuracao.documentosObrigatorios.length; i++) {
                        if (i != x && $scope.configuracao.documentosObrigatorios[i].descricao == $scope.configuracao.documentosObrigatorios[x].descricao) {
                            Notificacao.warn('Operação não permitida!', 'Não é permitido adicionar mais de um documento com mesmo nome!');
                            return;
                        }
                    }
                }
            }
            if ($scope.configuracao.id != null) {
                Configuracao.update($scope.configuracao, onSaveFinished);
            } else {
                Configuracao.save($scope.configuracao, onSaveFinished);
            }
        };

        function limparIntervaloIntegracao() {
            if ($scope.configuracao.urlConexao == undefined || $scope.configuracao.urlConexao.trim() == '') {
                $scope.configuracao.intervaloIntegracao = null;
            }
        }

        $scope.onBlurUrlConexao = function () {
            limparIntervaloIntegracao();
        };

        $scope.onBlurIntervaloIntegracao = function () {
            limparIntervaloIntegracao();
        };

        $scope.limparDescricao = function () {
            $scope.documentoObrigatorio.descricao = "";
        };

        $scope.adicionarDocumentoObrigatorio = function () {
            if (!$scope.configuracao.documentosObrigatorios) {
                $scope.configuracao.documentosObrigatorios = [];
            }

            if ($scope.configuracao.documentosObrigatorios.length > 0) {
                for (var i = 0; i < $scope.configuracao.documentosObrigatorios.length; i++) {
                    if ($scope.configuracao.documentosObrigatorios[i].descricao == $scope.documentoObrigatorio.descricao) {
                        Notificacao.warn("Operação não permitida!", "O documento já foi adicionado!");
                        return;
                    }
                }
            }
            $scope.configuracao.documentosObrigatorios.push($scope.documentoObrigatorio);

            $scope.documentoObrigatorio = getNovoDocumentoObrigatorio();
        };

        function getNovoDocumentoObrigatorio() {
            return {
                descricao: ""
            };
        }

        $scope.removerDocumentoObrigatorio = function (documento) {
            var indice = $scope.configuracao.documentosObrigatorios.indexOf(documento);
            $scope.configuracao.documentosObrigatorios.splice(indice, 1);
        }
    })
;
})();