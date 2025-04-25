(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ConstrucaoCivilController', function ($modal, $state, $scope, localStorageService,
                                                       ConstrucaoCivil, ParseLinks, Notificacao) {
        $scope.obras = [];
        $scope.per_page = 10;
        $scope.searchQuery = "";

        $scope.loadAll = function () {
            console.log('loadAll');
            var prestador = localStorageService.get("prestadorPrincipal");
            ConstrucaoCivil.buscarConstrucaoCivilFromPrestador({
                prestador: prestador.id,
                page: $scope.page,
                per_page: $scope.per_page,
                filtro: $scope.searchQuery
            }, function (result, headers) {
                console.log('Obras: ', $scope.obras);
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.obras = result;
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clean();
        };


        $scope.remover = function (id) {
            Notificacao.confirm('Atenção!', 'Tem certeza que remover esse registro?', 'warning',
                function () {
                    ConstrucaoCivil.delete({id: id}, function () {
                        Notificacao.info("Removido!", "O registro foi removido com sucesso.");
                        $scope.refresh();
                    }, function () {
                        Notificacao.info("Falha ao Remover!", "O registro possui dependências.");
                    });
                })
        };

        $scope.buscarClasseStatus = function (status) {
            switch (status) {
                case "NAO_INICIADA":
                    return "label label-primary";
                case "EM_ANDAMENTO":
                    return "label label-info";
                case "APROVADA":
                    return "label label-sucess";
                case "CONCLUIDA":
                    return "label label-important";
                case "PARALIZADA":
                    return "label label-danger";
            }
        };
    })
;
})();