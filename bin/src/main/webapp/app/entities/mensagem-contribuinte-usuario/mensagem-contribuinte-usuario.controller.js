(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('MensagemContribuinteUsuarioController', function ($scope, MensagemContribuinteUsuarioService, $modal,
                                                                       SweetAlert, Principal, $sanitize, ParseLinks,
                                                                       localStorageService) {

            $scope.mensagens = [];
            $scope.account = {};
            $scope.page = 0;
            $scope.per_page = 10;
            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");

            $scope.trustAsHtml = function (value) {
                return $sanitize(value);
            };

            $scope.montarConsulta = function () {
                var campos = [];
                var parametroQueryCampo = {
                    campo: "ce.id",
                    operador: "IGUAL",
                    valorLong: $scope.prestadorPrincipal.prestador.id
                };
                campos.push(parametroQueryCampo);
                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }]
                };
            }

            $scope.loadAll = function () {
                MensagemContribuinteUsuarioService.consultar($scope.montarConsulta(),
                    function (result, headers) {
                        $scope.mensagens = result;
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                    }
                );
            };

            $scope.loadAll();

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };
        });
})();
