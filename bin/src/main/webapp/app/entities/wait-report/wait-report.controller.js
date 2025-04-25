(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('WaitReportController', function ($scope, $modalInstance, $timeout, uuid, Report,
                                                      ImpressaoPdf) {
            $scope.uuid = uuid;
            $scope.webReport = null;
            $scope.timeout = null;

            $scope.getWebReport = function () {
                console.log('get uuid', $scope.uuid);
                Report.getByUuid({uuid: $scope.uuid}, function (data) {
                    $scope.webReport = data;
                    if (!$scope.webReport.fim) {
                        $scope.timeout = $timeout(function () {
                            $scope.getWebReport();
                        }, 5000)
                    } else {
                        $timeout.cancel($scope.timeout);
                    }
                });
            }

            $scope.getWebReport();

            $scope.imprimir = function () {
                $modalInstance.close(false);
                ImpressaoPdf.imprimirPdfViaUrl('/spring/report/imprimir/' + $scope.webReport.uuid);
            }

            $scope.cancel = function () {
                $modalInstance.close(false);
            }

            $scope.$on("$destroy", function (event) {
                $timeout.cancel($scope.timeout);
            });
        });
})();
