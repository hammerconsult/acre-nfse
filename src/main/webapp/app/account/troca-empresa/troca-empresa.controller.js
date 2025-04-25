(function () {
'use strict';

angular.module('nfseApp')
    .controller('TrocaEmpresaController', function ($scope, $state, $window, ParseLinks, SweetAlert, $modalInstance, localStorageService, PrestadorServicos, Principal) {

        $scope.empresas = [];
        $scope.filtro = "";
        $scope.per_page = 20;

        $scope.loadAll = function () {
            PrestadorServicos.getPrestadoresUsuario({
                filtro: $scope.filtro,
                login: $scope.account.login,
                page: $scope.page ? $scope.page : 0,
                per_page: $scope.per_page
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.empresas = result;
            });
            $scope.empresaSelecionada = localStorageService.get("prestadorPrincipal");
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        Principal.identity().then(function (account) {
            if (account) {
                $scope.account = account;
                $scope.loadAll();
            }
        });

        $scope.ok = function (empresa) {
            PrestadorServicos.trocarEmpresa({login: $scope.account.login, prestadorId: empresa.id}, function () {
                localStorageService.set('prestadorPrincipal', empresa);
                Principal.identity(true).then(function (account) {
                    if (account) {
                        $scope.account = account;
                        $scope.$emit('nfseApp:navbarUpdate', empresa);
                        $state.go('home');
                        $modalInstance.dismiss('cancel');
                    }
                });
            });

        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.irParaCredenciamento = function () {
            $scope.cancel();
            $state.go("credenciamento")
        };
    });
})();