(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PrestadorServicosDetailController', function ($scope, $rootScope, $stateParams, entity, PrestadorServicos) {
        $scope.prestadorServicos = entity;
        $scope.load = function (id) {
            PrestadorServicos.get({id: id}, function (result) {
                $scope.prestadorServicos = result;
            });
        };
        $rootScope.$on('nfseApp:prestadorServicosUpdate', function (event, result) {
            $scope.prestadorServicos = result;
        });

        function getBlobFromBase64(arquivo) {
            /*data:{{vm.arquivo.contentType}};base64,{{vm.arquivo.bytes}}*/
            var byteCharacters = atob(arquivo.conteudo);
            var byteNumbers = new Array(byteCharacters.length);
            for (var i = 0; i < byteCharacters.length; i++) {
                byteNumbers[i] = byteCharacters.charCodeAt(i);
            }
            var byteArray = new Uint8Array(byteNumbers);
            var blob = new Blob([byteArray], {type: arquivo.contentType});
            return blob;
        }

        $scope.baixarArquivo = function (index) {
            var documentoApresentado = $scope.prestadorServicos.documentosApresentados[index];
            if (documentoApresentado.arquivo) {
                var fileName = documentoApresentado.arquivo.nome;
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.style = "display: none";

                var file = getBlobFromBase64(documentoApresentado.arquivo);
                var fileURL = window.URL.createObjectURL(file);
                a.href = fileURL;
                a.download = fileName;
                a.click();
            }
        };
    });
})();