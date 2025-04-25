(function () {
    'use strict';

angular.module('nfseApp')
    .controller('AidfeListAnaliseController', function ($scope, Aidfe, AidfeSearch, $filter, ParseLinks, SweetAlert) {
        $scope.aidfes = [];
        $scope.page = 1;
        $scope.per_page = 10;
        $scope.situacoes = Aidfe.getSituacoes();
        $scope.tabSituacaoCorrente = 'AGUARDANDO';
        $scope.loadAll = function (situacao) {
            $scope.tabSituacaoCorrente = situacao;
            Aidfe.queryPorSituacao({
                page: $scope.page,
                per_page: $scope.per_page,
                situacaoAIDFE: situacao
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.aidfes = result;
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll($scope.tabSituacaoCorrente);
        };

        $scope.loadAll($scope.tabSituacaoCorrente);

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
                    $scope.loadAll();
                    SweetAlert.swal("Removido!", "A Solicitação de AIDF-e foi removido com sucesso.", "success");
                });
        };

        $scope.search = function (search, situacao) {
            if (!search) {
                search = '';
            }
            AidfeSearch.buscarPorSituacao({query: search, situacaoAIDFE: situacao},
                function (result) {
                    console.log("result ", result);
                    if (result.length == 0) {
                        $scope.loadAll(situacao);
                    } else {
                        $scope.aidfes = result;
                    }
                }, function (response) {
                    console.log("response ", response);
                    if (response.status === 404) {
                        $scope.loadAll(situacao);
                    }
                }
            )
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clean();
        };

        $scope.clear = function () {
            $scope.aidfe = {id: null};
        };

        $scope.classTab = function (situacao) {
            if (situacao == 'AGUARDANDO') {
                return 'text-warning';
            }
            if (situacao == 'DEFERIDA') {
                return 'text-success';
            }
            if (situacao == 'PARCIALMENTE') {
                return 'text-info';
            }
            if (situacao == 'INDEFERIDA') {
                return 'text-danger';
            }
        }
    });
})();