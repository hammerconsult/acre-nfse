(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('Util', function ($resource, $window) {
            var service = this;
            service.apenasNumeros = function (valor) {
                return valor.replace(/[^0-9]+/g, '');
            };
            service.getMeses = function () {
                return [
                    {name: "JANEIRO", descricao: "Janeiro", numeroMes: 1},
                    {name: "FEVEREIRO", descricao: "Fevereiro", numeroMes: 2},
                    {name: "MARCO", descricao: "Março", numeroMes: 3},
                    {name: "ABRIL", descricao: "Abril", numeroMes: 4},
                    {name: "MAIO", descricao: "Maio", numeroMes: 5},
                    {name: "JUNHO", descricao: "Junho", numeroMes: 6},
                    {name: "JULHO", descricao: "Julho", numeroMes: 7},
                    {name: "AGOSTO", descricao: "Agosto", numeroMes: 8},
                    {name: "SETEMBRO", descricao: "Setembro", numeroMes: 9},
                    {name: "OUTUBRO", descricao: "Outubro", numeroMes: 10},
                    {name: "NOVEMBRO", descricao: "Novembro", numeroMes: 11},
                    {name: "DEZEMBRO", descricao: "Dezembro", numeroMes: 12}
                ];
            };
            service.getExercicios = function () {
                var primeiroExercicio = new Date().getFullYear();
                var exercicios = [];
                for (var i = primeiroExercicio; i >= (primeiroExercicio - 20); i--) {
                    exercicios.push(i);
                }
                return exercicios;
            };
            service.getExercicioAtual = function () {
                return new Date().getFullYear();
            };
            service.copyToClipboard = function (toCopy) {
                var body = angular.element($window.document.body);
                var textarea = angular.element('<textarea/>');
                textarea.css({
                    position: 'fixed',
                    opacity: '0'
                });

                textarea.val(toCopy);
                body.append(textarea);
                textarea[0].select();

                document.execCommand('copy');

                textarea.remove();
            };
            service.getNameMesFromNumero = function (numeroMes) {
                var meses = service.getMeses();
                for (var i = 0; i < meses.length; i++) {
                    if (meses[i].numeroMes == numeroMes) {
                        return meses[i].name;
                    }
                }
                return null;
            }
            return service;
        });
})();
