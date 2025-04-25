(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('DesifController', function ($scope, Configuracao, ManualService) {
            $scope.manuais = null;

            $scope.init = function () {
                Configuracao.get({}, function (data) {
                    if (data.tipoManualDesif && data.tipoManualDesif.id) {
                        ManualService.manuaisPorTipo({tipo: data.tipoManualDesif.id}, function (data) {
                            $scope.manuais = data;
                        });
                    }
                });
            }

            $scope.init();

        });
})();
