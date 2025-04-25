(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PerguntasRespostasController', function ($scope, PerguntasRespostasService) {

        $scope.perguntasPorAssunto = [];
        $scope.perguntasAndRespostas = [];

        function indexOfAssunto(assunto) {
            var indexOf = -1;
            angular.forEach($scope.perguntasPorAssunto, function (data) {
                if (data.assunto.id == assunto.id) {
                    indexOf = $scope.perguntasPorAssunto.indexOf(data);
                }
            });
            return indexOf;
        }

        function agruparPorAssunto(perguntas) {
            angular.forEach(perguntas, function (data) {
                var indexOf = indexOfAssunto(data.assuntoNfseDTO);
                if (indexOf == -1) {
                    $scope.perguntasPorAssunto.push({'assunto': data.assuntoNfseDTO, 'perguntas': []});
                    indexOf = 0;
                }
                $scope.perguntasPorAssunto[indexOf].perguntas.push(data);
            });
        }

        function buscarPerguntasRespostasParaExibicao() {
            PerguntasRespostasService.perguntasRespostasParaExibicao(function (data) {
                $scope.perguntasAndRespostas = data;
            });
        }

        buscarPerguntasRespostasParaExibicao();
    });
})();
