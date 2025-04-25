(function () {
    'use strict';

angular.module('nfseApp')
    .controller('UserManagementSearchCpfController', function ($scope, $modalInstance, User, ParseLinks,
                                                               Language, UserSearch, PrestadorServicos, Notificacao,
                                                               localStorageService) {
        $scope.users;
        $scope.authorities = ["ROLE_USER", "ROLE_ADMIN"];
        $scope.solicitacao = {dadosPessoais: {}};
        $scope.per_page = 10;
        $scope.page;
        $scope.links;

        Language.getAll().then(function (languages) {
            $scope.languages = languages;
        });

        $scope.search = function () {
            UserSearch.query({page: $scope.page, size: $scope.per_page, query: $scope.searchQuery},
                function (result, headers) {
                    if (headers)
                        $scope.links = ParseLinks.parse(headers('link'));
                $scope.users = result;
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.search();
        }

        $scope.ok = function (user) {
            $modalInstance.close(user);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.novo = function () {
            $scope.users = undefined;
            $scope.searchQuery = '';
            $scope.page = 0;
            $scope.per_page = 10;
        };

        $scope.emailSolicitandoCadastro = function () {
            $scope.solicitacao.prestadorServico = {id: localStorageService.get("prestadorPrincipal").prestador.id};
            PrestadorServicos.enviarEmailSolicitandoCadastro($scope.solicitacao, onSuccessEmailSolicitandoCadastro);
        };

        function onSuccessEmailSolicitandoCadastro() {
            Notificacao.info("Informação", "E-mail enviado com sucesso.");
            $scope.cancel();
        }
    });
})();