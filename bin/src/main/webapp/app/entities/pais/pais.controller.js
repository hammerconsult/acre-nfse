(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PaisController', function ($scope, Pais, PaisSearch, ParseLinks, SweetAlert) {
        $scope.paises = [];
        $scope.page = 1;
        $scope.per_page = 10;
        $scope.mostraBotaoLoadInicial = false;
        $scope.loadAll = function () {
            Pais.query({page: $scope.page, per_page: $scope.per_page}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.paises = result;
                $scope.mostraBotaoLoadInicial = 1;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Pais.get({id: id}, function (result) {
                $scope.pais = result;
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
            Pais.delete({id: id},
                function () {
                    $scope.loadAll();
                    SweetAlert.swal("Removido!", "O País foi removido com sucesso.", "success");
                });
        };

        $scope.search = function () {
            PaisSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.paises = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pais = {codigo: null, nome: null, id: null, sigla: null};
        };

        $scope.loadInitialData = function () {
            Pais.loadInitialData();
            $scope.loadAll();
        }
    });
})();