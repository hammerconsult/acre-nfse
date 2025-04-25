(function () {
    'use strict';

angular.module('nfseApp')
    .service('ExportarXls', function ($http, ImpressaoPdf) {

        this.getXls = function (dto) {
            var lista = [];
            angular.forEach(dto.linhas, function (rps) {
                var linha = {};
                for (var i = 0; i < dto.colunas.length; i++) {
                    var coluna = dto.colunas[i];
                    linha[coluna.valor] = rps[coluna.valor];
                }
                lista.push(linha);
            });
            dto.linhas = lista;
            $http.post('/api/exportar/xls', dto).then(function (result) {
                var a = document.createElement('a');
                a.href = 'data:application/csv;base64,' + result.data.conteudo;
                a.target = '_blank';
                a.download = 'filename.csv';
                document.body.appendChild(a);
                a.click();
            });
        };


        this.getPdf = function (dto) {
            var lista = [];
            angular.forEach(dto.linhas, function (rps) {
                var linha = {};
                for (var i = 0; i < dto.colunas.length; i++) {
                    var coluna = dto.colunas[i];
                    console.log(coluna);
                    console.log(rps);
                    console.log(rps[coluna.valor]);
                    linha[coluna.valor] = rps[coluna.valor];
                }
                lista.push(linha);
            });
            dto.linhas = lista;

            ImpressaoPdf.imprimirPdfViaPost('/api/exportar/pdf', dto);
        };


    });

})();