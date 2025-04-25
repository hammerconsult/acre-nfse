(function () {
    'use strict';

angular.module('nfseApp')
    .controller('AidfeController', function ($scope, $state, Aidfe, AidfeSearch, $filter, ParseLinks, SweetAlert, localStorageService) {

        $scope.aidfes = [];
        $scope.per_page = 10;
        $scope.searchQuery = "";

        $scope.loadAll = function () {
            var prestador = localStorageService.get("prestadorPrincipal");
            Aidfe.buscarTodasAidfesDoPrestador({
                prestador: prestador.id,
                page: $scope.page,
                per_page: $scope.per_page,
                filtro: $scope.searchQuery
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.aidfes = result;
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();

        $scope.delete = function (id) {
            Aidfe.get({id: id}, function (result) {
                $scope.aidfe = result;

                SweetAlert.swal({
                        title: "Confirme a exclusão",
                        text: "Você tem certeza que quer excluir essa Solicitação AIDF-e?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Excluir",
                        cancelButtonText: "Não, Cancelar",
                        closeOnConfirm: false,
                        closeOnCancel: false
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            $scope.confirmDelete(id);
                        } else {
                            SweetAlert.swal("Cancelado", "A solicitação de AIDF-e não foi removido :)", "error");
                        }
                    });
            });
        };

        $scope.confirmDelete = function (id) {
            Aidfe.delete({id: id},
                function () {
                    SweetAlert.swal("Removido!", "A Solicitação de AIDF-e foi removido com sucesso.", "success");
                    $state.reload();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clean();
        };

        $scope.clear = function () {
            $scope.aidfe = {id: null};
        };
    });
})();