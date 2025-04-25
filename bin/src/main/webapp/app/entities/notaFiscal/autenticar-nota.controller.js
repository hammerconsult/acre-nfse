(function () {
    'use strict';

    angular.module('nfseApp').controller('AutenticarNotaController',
        ['$rootScope', '$scope', '$modal', '$state', '$filter', '$stateParams', 'NotaFiscal', 'Notificacao', 'ImpressaoPdf', 'entity', 'Configuracao',
            function ($rootScope, $scope, $modal, $state, $filter, $stateParams, NotaFiscal, Notificacao, ImpressaoPdf, entity, Configuracao) {

                $scope.limpar = function () {
                    $scope.autenticar = {cpfCnpjPrestador: "", numeroNfse: "", codigoAutenticacao: ""};
                    $scope.valido = null;
                    $scope.nfse = null;
                };

                Configuracao.get({}, function (configuracao) {
                    this.configuracao = configuracao;
                });

                if (entity && entity.numero) {
                    $scope.autenticar = {
                        cpfCnpjPrestador: entity.prestador.pessoa.dadosPessoais.cpfCnpj,
                        numeroNfse: entity.numero,
                        codigoAutenticacao: entity.codigoVerificacao
                    };
                    $scope.nfse = entity;
                    $scope.valido = true;
                } else {
                    $scope.limpar();
                }

                $scope.openDateDialog = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.emissaoPopUp = true;
                };

                $scope.autenticarNota = function () {
                    $scope.codigoVerificacaoDiferente = false;
                    $scope.cancelado = false;

                    NotaFiscal.autenticar($scope.autenticar,
                        function (data) {
                            if (data.situacao === 'CANCELADA') {
                                Notificacao.warn("Documento Cancelado", "Os valores representam uma nota fiscal cancelada");
                                $scope.cancelado = true;
                            } else {
                                Notificacao.info("Válido", "Os valores representam uma nota fiscal emitida válida");
                            }
                            $scope.valido = true;
                            if (data.codigoVerificacao !== $scope.autenticar.codigoAutenticacao) {
                                $scope.codigoVerificacaoDiferente = true;
                            }
                            $scope.nfse = data;
                        }, function (error) {
                            Notificacao.error("Inválido", "Os valores informardos não representam uma nota fiscal emitida válida");
                            $scope.valido = false;
                        });
                };

                $scope.imprimirNota = function (id) {
                    ImpressaoPdf.imprimirPdfViaUrl('/publico/notaFiscal/imprime/' + id);
                };


            }])
    ;
})();