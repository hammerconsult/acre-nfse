(function () {
    'use strict';

angular.module('nfseApp')
    .controller('FooterController',
        function ($scope, $rootScope, $location, $state, $modal, ManualService) {
            $scope.$state = $state;
            //
            // $rootScope.$on('$stateChangeSuccess',
            //     function (event, toState) {
            //         ManualService.manuaisPorTag({tag: toState.name}, function (data) {
            //             $scope.manuaisDaTela = data;
            //         })
            //     });


            $scope.abrirManuais = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/externo/manual/manuais.html',
                    controller: 'ManuaisController',
                    size: 'lg',
                    resolve: {
                        manuais: function () {
                            return $scope.manuaisDaTela;
                        }
                    }
                });

            };

        })
;
})();