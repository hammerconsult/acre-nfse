(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PrestadorServicosController', function ($scope, PrestadorServicos, PrestadorServicosSearch, ParseLinks, SweetAlert) {
        $scope.prestadorServicoss = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            PrestadorServicos.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.prestadorServicoss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PrestadorServicos.get({id: id}, function(result) {
                $scope.prestadorServicos = result;
                SweetAlert.swal({
                        title: "Confirme a exclusão",
                        text: "Você tem certeza que quer excluir o registro selecionado?",
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
                            SweetAlert.swal("Cancelado", "O registro não foi removido :)", "error");
                        }
                    });
            });
        };

        $scope.confirmDelete = function (id) {
            PrestadorServicos.delete({id: id},
                function () {
                    $scope.loadAll();
                    SweetAlert.swal("Removido!", "O registro foi removido com sucesso.", "success");
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PrestadorServicosSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.prestadorServicoss = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.prestadorServicos = {id: null};
        };
    });
})();