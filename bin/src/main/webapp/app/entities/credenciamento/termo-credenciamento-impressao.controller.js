(function () {
    'use strict';

angular.module('nfseApp')
    .controller('TermoCredenciamentoImpressaoController',
    function ($scope, $rootScope, $http, $sce, id, NotaFiscal, $modalInstance) {
        $scope.load = function () {
            $http.get('/api/credenciamento/imprime/' + id, {responseType: 'arraybuffer'})
                .then(function (result) {
                    console.log(result);
                    var file = new Blob([result.data], {type: 'application/pdf'});
                    $scope.pdfUrl = window.URL.createObjectURL(file);
                    $scope.pdfName = 'Nota Fiscal';
                });
        };

        $scope.load();

        $scope.scroll = 0;
        $scope.loading = 'Carregando';

        $scope.onError = function(error) {
            console.log(error);
        };

        $scope.onLoad = function() {
            $scope.loading = '';
        };

        $scope.onProgress = function(progress) {
            console.log(progress);
        };

        $scope.print = function () {
            var canvas = document.getElementById('pdf');
            var win=window.open();
            win.document.write("<br><img src='"+canvas.toDataURL()+"'>");
            win.print();
            win.location.reload();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
})();