(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PessoaController', function ($scope, Pessoa, PessoaSearch, ParseLinks, SweetAlert, Notificacao) {
        $scope.pessoas = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Pessoa.query({page: $scope.page, per_page: 10}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.pessoas = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Pessoa.get({id: id}, function (result) {
                $scope.pessoa = result;
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
            Pessoa.delete({id: id},
                function () {
                    SweetAlert.swal("Removido!", "O registro foi removido com sucesso.", "success");
                    $scope.loadAll();
                    $scope.clear();

                });
        };

        $scope.search = function () {
            PessoaSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.pessoas = result;
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
            $scope.pessoa = {id: null};
        };
    });
})();