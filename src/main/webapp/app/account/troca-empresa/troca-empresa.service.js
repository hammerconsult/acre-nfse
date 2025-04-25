(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('TrocaEmpresa', TrocaEmpresa);

    function TrocaEmpresa($modal) {

        var service = {
            open: open,
            modalInstance: null,
            isOpen: false
        };
        return service;

        function open() {
            if (service.isOpen) {
                return;
            }
            service.modalInstance = $modal.open({
                templateUrl: 'app/account/troca-empresa/troca-empresa.html',
                controller: 'TrocaEmpresaController',
                animation: true,
                keyboard: false,
                backdrop: 'static',
                size: 'lg'
            });
            service.isOpen = true;
            service.modalInstance.result.then(function (empresa) {
                console.log("Selecionou a empresa", empresa);
                service.isOpen = false;
            }, function () {
                service.isOpen = false;
            });
        }
    }
})();


