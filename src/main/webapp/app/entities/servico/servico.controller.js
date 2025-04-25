(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ServicoController', function ($scope, Servico, ServicoSearch, ParseLinks, SweetAlert) {
        $scope.servicos = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Servico.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.servicos = result;
            });
        };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.confirmDelete = function (id) {
            Servico.delete({id: id},
                function () {
                    $scope.loadAll();
                    SweetAlert.swal("Removido!", "O registro foi removido com sucesso.", "success");
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ServicoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.servicos = result;
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
            $scope.servico = {codigo: null, descricao: null, aliquota: null, id: null};
        };

        $scope.delete = function (id) {
            Servico.get({id: id}, function (result) {
                $scope.servico = result;
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
    });
})();