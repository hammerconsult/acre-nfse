(function () {
    'use strict';

angular.module('nfseApp')
    .controller('DeclaracaoMensalServicoController',
        function ($scope, DeclaracaoMensalServico, DeclaracaoMensalServicoSearch, ParseLinks, Notificacao,
                  ImpressaoPdf, localStorageService, Util) {

            $scope.page = 1;
            $scope.per_page = 999;
            $scope.declaracaoMensalServicos = [];
            $scope.exerciciosDeclarados = [];
            $scope.empresa = localStorageService.get("prestadorPrincipal");
            $scope.meses = Util.getMeses();
            $scope.exercicio = Util.getExercicioAtual();

            function montarConsultaGenerica(ano, mes) {
                var campos = [];

                campos.push({
                    campo: "dms.prestador_id",
                    operador: "IGUAL",
                    valorLong: $scope.empresa.prestador.id
                });

                campos.push({
                    campo: "e.ano",
                    operador: "IGUAL",
                    valorInteger: ano
                });

                campos.push({
                    campo: "dms.mes",
                    operador: "IGUAL",
                    valorString: Util.getNameMesFromNumero(mes)
                });

                campos.push({
                    campo: "dms.tipomovimento",
                    operador: "IN",
                    valorListString: ["NORMAL", "ISS_RETIDO"]
                });

                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                    orderBy: " order by dms.id desc "
                };
            }

            $scope.loadAll = function () {
                $scope.exerciciosDeclarados = [];

                var exercicioDeclarado = {ano: $scope.exercicio, mesesDeclarados: []};

                for (var j = $scope.mesInicial.numeroMes; j <= $scope.mesFinal.numeroMes; j++) {
                    var mesDeclarado = {mes: j, declaracoes: []};
                    exercicioDeclarado.mesesDeclarados.push(mesDeclarado);
                }
                exercicioDeclarado.mesesDeclarados.sort(function (a, b) {
                    return b.mes - a.mes;
                });
                $scope.exerciciosDeclarados.push(exercicioDeclarado);

                $scope.exerciciosDeclarados.sort(function (a, b) {
                    return b.ano - a.ano;
                });

                angular.forEach($scope.exerciciosDeclarados, function (exercicio) {
                    angular.forEach(exercicio.mesesDeclarados, function (mes) {
                        DeclaracaoMensalServico.consultar(montarConsultaGenerica(exercicio.ano, mes.mes), function (result) {
                            mes.declaracoes = result;
                        });
                    })
                });
            };

            function init() {
                var dataAtual = new Date();
                angular.forEach($scope.meses, function (mes) {
                    if (mes.numeroMes == 1) {
                        $scope.mesInicial = mes;
                    }
                    if (mes.numeroMes == dataAtual.getMonth() + 1) {
                        $scope.mesFinal = mes;
                    }
                });
                $scope.loadAll();
            }

            init();

            $scope.getMesPorNumero = function (numero) {
                for (var i = 0; i < $scope.meses.length; i++) {
                    if ($scope.meses[i].numeroMes === numero) {
                        return $scope.meses[i].descricao;
                    }
                }
            };

            $scope.loadPage = function (mes) {
                $scope.page = page;
                $scope.loadAll();
            };


            $scope.delete = function (id) {
                DeclaracaoMensalServico.get({id: id}, function (result) {
                    $scope.declaracaoMensalServico = result;

                    Notificacao.confirmDelete(
                        function (success) {
                            console.log('Notificacao.confirmDelete - success: ', success);
                            $scope.confirmDelete(id);
                        },
                        function (error) {
                            console.log('Notificacao.confirmDelete - error: ', error);
                        }
                    );
                });
            };

            $scope.confirmDelete = function (id) {
                DeclaracaoMensalServico.delete({id: id},
                    function () {
                        $scope.loadAll();
                        $scope.clear();
                    });
            };


            $scope.refresh = function () {
                $scope.loadAll();
                $scope.clear();
            };

            $scope.clear = function () {
                $scope.declaracaoMensalServico = {codigo: null, competencia: null, id: null};
            };

            $scope.imprimirDam = function (id) {
                ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-dam-declaracao/' + id);
            };

            $scope.imprimeRelatorioDMS = function (ano, mes) {
                ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-relatorio-dms/' + ano + "/" + mes + "/NORMAL");
            };

            $scope.hasDeclaracaoNaoCancelada = function (declaracoes) {
                for (var i = 0; i < declaracoes.length; i++) {
                    if (declaracoes[i].situacao != 'CANCELADO')
                        return true;
                }
                return false;
            };

            $scope.cancelar = function (id) {
                Notificacao.confirm('Atenção!', 'Tem certeza que deseja cancelar essa declaração?', 'warning',
                    function () {
                        DeclaracaoMensalServico.cancelar({id: id}, function () {
                            $scope.loadAll();
                            Notificacao.info('Operação Realizada.', 'Declaração Mensal de Serviço Cancelada!');
                        });
                    })
            };
        })
;
})();
