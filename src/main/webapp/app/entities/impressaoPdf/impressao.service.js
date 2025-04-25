(function () {
    'use strict';

angular.module('nfseApp')
    .service('ImpressaoPdf', function ($modal, $http, Notificacao) {

        this.imprimirPdfViaUrl = function (url) {
            $http.get(url, {responseType: 'arraybuffer'}).then(function (data) {
                abrirModal(data);
            });
        };

        this.imprimirPdfViaPost = function (url, data) {
            $http.post(url, data, {responseType: 'arraybuffer'}).then(function (response) {
                if (response.data.byteLength == 0) {
                    Notificacao.warn('Atenção', 'Nenhum registro para ser impresso.');
                } else {
                    abrirModal(response);
                }
            });
        };

        function abrirModal(data) {
            $modal.open({
                templateUrl: 'app/entities/impressaoPdf/dialog.html',
                controller: 'ImpressaoPDFController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                windowClass: 'modal-giant',
                resolve: {
                    data: [function () {
                        return data;
                    }]
                }
            })
        }
    });
})();