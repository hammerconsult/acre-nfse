(function () {
    'use strict';
angular.module('nfseApp')
    .controller('ImpressaoPDFController',
        function ($scope, $rootScope, $http, $sce, data, $modalInstance) {

            var vm = this;
            vm.arquivo = data;
            var file = new Blob([vm.arquivo.data], {type: 'application/pdf'});
            var fileURL = window.URL.createObjectURL(file);
            vm.conteudo = $sce.trustAsResourceUrl(fileURL);
            vm.cancel = cancel;
            vm.baixarAnexo = baixarAnexo;

            function cancel() {
                $modalInstance.dismiss('close');
            }

            function baixarAnexo() {
                var a = document.createElement('a');
                a.href = fileURL;
                a.target = '_blank';
                a.download = 'download-iss-onlione.pdf';
                document.body.appendChild(a);
                a.click();
            }

        })
;
})();

