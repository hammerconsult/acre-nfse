(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PesquisaCnaeController', function ($scope, Cnae, Servico, ParseLinks, ExportarXls) {

        $scope.links = null;
        $scope.page = null;
        $scope.filtro = "";
        $scope.cnaes = null;
        $scope.per_page = 10;
        $scope.pesquisarPorCnae = true;

        $scope.buscarCneaOrServico = function () {
            $scope.cnaes = null;
            if ($scope.pesquisarPorCnae)
                Cnae.getPorServicoCodigoDescricao({
                    page: $scope.page,
                    per_page: $scope.per_page,
                    filtro: $scope.filtro
                }, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.cnaes = result;
                });
            else
                Servico.getPorCneaCodigoDescricao({
                    page: $scope.page,
                    per_page: $scope.per_page,
                    filtro: $scope.filtro
                }, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.cnaes = result;
                });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.buscarCneaOrServico();
        };

        $scope.baixar = function (parametro) {
            var conteudo = {
                colunas: [
                    {descricao: 'Código', valor: 'codigo'},
                    {descricao: 'Descrição', valor: 'descricao'}
                ],
                linhas: $scope.cnaes
            };
            switch (parametro) {
                case 'pdf':
                    ExportarXls.getPdf(conteudo);
                    break;
                case 'xls':
                    ExportarXls.getXls(conteudo);
            }

        };
    });
})();
