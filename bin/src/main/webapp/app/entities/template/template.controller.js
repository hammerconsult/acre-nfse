(function () {
    'use strict';

angular.module('nfseApp')
    .controller('TemplateController', function ($scope, Template, TemplateSearch, ParseLinks, SweetAlert) {
        $scope.templates = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Template.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.templates = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Template.get({id: id}, function (result) {
                $scope.template = result;
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
            Template.delete({id: id},
                function () {
                    $scope.loadAll();
                    SweetAlert.swal("Removido!", "O registro foi removido com sucesso.", "success");
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TemplateSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.templates = result;
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
            $scope.template = {codigo: null, descricao: null, ativo: null, id: null};
        };
    });
})();
