(function () {
    'use strict';

angular.module('nfseApp')
    .controller('CnaeController', function ($scope, Cnae, CnaeSearch, ParseLinks, SweetAlert) {
        $scope.cnaes = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Cnae.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.cnaes = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Cnae.get({id: id}, function (result) {
                $scope.cnae = result;
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
            Cnae.delete({id: id},
                function () {
                    $scope.loadAll();
                    SweetAlert.swal("Removido!", "O registro foi removido com sucesso.", "success");
                    $scope.clear();
                });
        };

        $scope.search = function () {
            CnaeSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.cnaes = result;
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
            $scope.cnae = {codigo: null, descricao: null, ativo: null, id: null};
        };
    });
})();