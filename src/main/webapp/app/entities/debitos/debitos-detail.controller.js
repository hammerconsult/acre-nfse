(function () {
    'use strict';

angular.module('nfseApp')
    .controller('DebitosDetailController', function ($scope, debito, NotaFiscal, PrestadorServicos,
                                                     ImpressaoPdf, $modal, localStorageService, DeclaracaoMensalServico) {
        $scope.debito = debito;
        $scope.notas = [];

        $scope.imprimirComposicaoGuia = function() {
            $scope.agrupador = {parcelas: []};
            $scope.agrupador.parcelas.push($scope.debito);
            PrestadorServicos.buscarDamDaParcela($scope.agrupador, function (dam) {
                ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-composicao-guia-nfse/'
                    + localStorageService.get("prestadorPrincipal").prestador.id + '/' + dam.id);
            });
        };

        function carregarNotas() {
            NotaFiscal.getPorCalculo({calculoId: $scope.debito.idCalculo}, function (data) {
                $scope.notas = data;
            });
        }

        function carregarDms() {
            DeclaracaoMensalServico.getByCalculoId({calculoId: $scope.debito.idCalculo}, function (data) {
                $scope.dms = data;
            });
        }

        carregarDms();
        carregarNotas();

        $scope.imprimirDam = function () {
            var idsParcelas = [];
            idsParcelas.push($scope.debito.idParcela);
            ImpressaoPdf.imprimirPdfViaPost('/api/imprimir-dam', idsParcelas);
        };

        $scope.enviarDam = function () {
            $scope.agrupador = {parcelas: []};
            $scope.agrupador.parcelas.push($scope.debito);
            var modalEnviarNota = $modal.open({
                templateUrl: 'app/entities/debitos/enviar-dam-email.html',
                controller: 'EnviarDamEmailController',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return $scope.agrupador
                    }
                }
            });

            modalEnviarNota.result.then(function (data) {
                if (data) {
                    console.log(data)
                }
            });
        };
    });})();