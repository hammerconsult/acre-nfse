(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('ManualController', function ($scope, ManualService, $http, $sce) {

            $scope.manuaisPorTipo = {};
            $scope.tipos = [];
            $scope.manualSelecionado = null;

            $scope.exibirManuais = function (tipo) {
                tipo.exibir = !tipo.exibir;
                $scope.manualSelecionado = null;
                if (tipo.exibir) {
                    if ($scope.manuaisPorTipo[tipo.id].length > 0) {
                        $scope.buscarArquivo($scope.manuaisPorTipo[tipo.id][0]);
                    }
                }
            }

            $scope.buscarArquivo = function (manual) {
                $scope.manualSelecionado = manual;
                $scope.conteudo = null;
                if (manual.arquivo && manual.arquivo.id) {
                    $http.get("/api/arquivo/" + manual.arquivo.id, {responseType: 'arraybuffer'}).then(function (data) {
                        var arquivo = data;
                        var file = new Blob([arquivo.data], {type: 'application/pdf'});
                        var fileURL = window.URL.createObjectURL(file);
                        $scope.conteudo = $sce.trustAsResourceUrl(fileURL);
                    });
                }
            }

            $scope.isPdf = function () {
                if ($scope.manualSelecionado &&
                    $scope.manualSelecionado.arquivo) {
                    return $scope.manualSelecionado.arquivo.nome.toLowerCase().endsWith('pdf');
                }
                return false;
            }

            $scope.baixarAnexo = function () {
                var a = document.createElement('a');
                a.href = $scope.fileURL;
                a.target = '_blank';
                a.download = 'manual.pdf';
                document.body.appendChild(a);
                a.click();
            }

            function buscarManuaisParaExibicao() {
                ManualService.manuaisParaExibicao(function (data) {
                    for (var i = 0; i < data.length; i++) {
                        var manual = data[i];
                        if (!$scope.manuaisPorTipo[manual.tipoManualDTO.id]) {
                            $scope.manuaisPorTipo[manual.tipoManualDTO.id] = [];
                            $scope.tipos.push(manual.tipoManualDTO);
                        }
                        $scope.manuaisPorTipo[manual.tipoManualDTO.id].push(manual);
                    }
                    $scope.tipoManual = $scope.tipos[0];
                });

            }

            buscarManuaisParaExibicao();
        });
})();
