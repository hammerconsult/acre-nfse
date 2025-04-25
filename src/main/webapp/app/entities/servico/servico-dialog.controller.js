(function () {
    'use strict';

angular.module('nfseApp').controller('ServicoDialogController',
    ['$scope', '$state', '$stateParams', 'entity', 'Servico', 'Cnae', 'ParseLinks', 'Notificacao',
        function ($scope, $state, $stateParams, entity, Servico, Cnae, ParseLinks, Notificacao) {

            $scope.loadAllCnaes = function () {
                Cnae.query({page: $scope.page, per_page: 1000}, function (result, headers) {
                    $scope.cnaes = result;
                });
            };

            $scope.servico = entity;
            $scope.load = function (id) {
                Servico.get({id: id}, function (result) {
                    $scope.servico = result;
                });
            };

            $scope.loadAllCnaes();

            var onSaveFinished = function (result) {
                $state.go("servico");
                Notificacao.info("Serviço salvo com sucesso.");
            };

            $scope.save = function () {
                if ($scope.servico.id != null) {
                    Servico.update($scope.servico, onSaveFinished);
                } else {
                    Servico.save($scope.servico, onSaveFinished);
                }
            };

            $scope.clear = function () {

            };
        }]);
})();