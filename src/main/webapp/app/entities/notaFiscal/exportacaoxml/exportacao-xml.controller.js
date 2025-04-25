(function () {
    'use strict';

    angular.module('nfseApp').controller('ExportacaoXmlController',
        function ($scope, PrestadorServicos, Notificacao, localStorageService, DataUtil, NotaFiscal, $timeout,
                  Util, pagingParams) {
            $scope.prestador = null;
            $scope.tipoDocumento = pagingParams && pagingParams.tipo? pagingParams.tipo: 'EMITIDA';

            $scope.verificarBaixarXml = verificarBaixarXml;
            $scope.tipo = "NORMAL";
            $scope.tipoData = "COMPETENCIA";
            $scope.dataInicial;
            $scope.dataFinal;
            $scope.competenciaAno;
            $scope.competenciaMes;
            $scope.numeroNotaFiscal;
            $scope.meses;
            $scope.baixarXml = null;

            PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (data) {
                $scope.prestador = data;
            });

            $scope.meses = Util.getMeses({});

            $scope.montarConsultaNotaFiscal = function (orderBy) {
                var campos = [];

                if($scope.tipoDocumento === 'EMITIDA'){
                    var parametroQueryCampo = {
                        campo: "obj.prestador_id",
                        operador: "IGUAL",
                        valorLong: $scope.prestador.id
                    };
                    campos.push(parametroQueryCampo);
                }else{
                    if ($scope.prestador) {
                        var parametroQueryCampo = {
                            campo: "dptnf.cpfcnpj",
                            operador: "IGUAL",
                            valorString: Util.apenasNumeros($scope.prestador.pessoa.dadosPessoais.cpfCnpj)
                        };
                        campos.push(parametroQueryCampo);
                    } else {
                        var parametroQueryCampo = {
                            campo: "dptnf.cpfcnpj",
                            operador: "IGUAL",
                            valorString: Util.apenasNumeros($scope.account.login)
                        };
                        campos.push(parametroQueryCampo);
                    }
                }

                if ($scope.tipoData == "EMISSAO") {
                    if ($scope.dataInicial) {
                        var dataInicial = DataUtil.dateWithOutHoursMinutesSeconds($scope.dataInicial);
                        parametroQueryCampo = {
                            campo: "trunc(obj.emissao)",
                            operador: "MAIOR_IGUAL",
                            valorDate: dataInicial
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.dataFinal) {
                        var dataFinal = DataUtil.dateWithOutHoursMinutesSeconds($scope.dataFinal);
                        parametroQueryCampo = {
                            campo: "trunc(obj.emissao)",
                            operador: "MENOR_IGUAL",
                            valorDate: dataFinal
                        };
                        campos.push(parametroQueryCampo);
                    }
                } else if ($scope.tipoData == "COMPETENCIA") {
                    parametroQueryCampo = {
                        campo: "extract(year from dps.competencia)",
                        operador: "IGUAL",
                        valorInteger: $scope.competenciaAno
                    };
                    campos.push(parametroQueryCampo);
                    parametroQueryCampo = {
                        campo: "extract(month from dps.competencia)",
                        operador: "IGUAL",
                        valorInteger: $scope.competenciaMes.numeroMes
                    };
                    campos.push(parametroQueryCampo);
                }

                if ($scope.numeroNotaFiscal) {
                    parametroQueryCampo = {
                        campo: " obj.numero ",
                        operador: "IGUAL",
                        valorLong: $scope.numeroNotaFiscal
                    };
                    campos.push(parametroQueryCampo);
                }
                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                    orderBy: orderBy
                };
            };

            function filtrosValidos() {
                if ($scope.tipoData == "EMISSAO" && !$scope.dataInicial) {
                    Notificacao.warn("Atenção", "A Emissão Inicial deve ser informada.");
                    return false;
                }
                if ($scope.tipoData == "EMISSAO" && !$scope.dataFinal) {
                    Notificacao.warn("Atenção", "A Emissão Final deve ser informada.");
                    return false;
                }
                if ($scope.tipoData == "COMPETENCIA" && !$scope.competenciaMes) {
                    Notificacao.warn("Atenção", "A Competência deve ser informada.");
                    return false;
                }
                if ($scope.tipoData == "COMPETENCIA" && !$scope.competenciaAno) {
                    Notificacao.warn("Atenção", "O Ano deve ser informado.");
                    return false;
                }
                return true;
            }

            $scope.consultarXml = function () {
                NotaFiscal.consultarXml($scope.baixarXml, function (result) {
                    if (result) {
                        $scope.baixarXml.percentual = result.percentual;
                        $scope.baixarXml.conteudo = result.conteudo;
                        $scope.baixarXml.erro = result.erro;
                    }
                    if ($scope.baixarXml.erro || $scope.baixarXml.percentual >= 100) {
                        $timeout.cancel($scope.timeout);
                    } else {
                        verificarSituacaoXml();
                    }
                }, function () {
                    $timeout.cancel($scope.timeout);
                });
            };

            $scope.exportarXml = function () {
                if (!filtrosValidos())
                    return;
                $scope.baixarXml = {
                    tipo: $scope.tipo,
                    consultaGenericaNfseDTO: $scope.montarConsultaNotaFiscal(" order by obj.numero desc "),
                    inscricao: $scope.prestador.inscricaoMunicipal,
                    percentual: 0
                };
                NotaFiscal.baixarXml($scope.baixarXml, function () {
                    verificarSituacaoXml()
                });
            };

            function verificarBaixarXml() {
                if ($scope.baixarXml.percentual >= 100) {
                    $scope.baixandoArquivo = true;
                    var a = document.createElement('a');
                    a.href = 'data:application/xml;base64,' + $scope.baixarXml.conteudo;
                    a.target = '_blank';
                    a.download = 'lote-notas-fiscais.xml';
                    document.body.appendChild(a);
                    a.click();
                    $scope.baixandoArquivo = false;
                    $scope.baixarXml = null;
                    $timeout.cancel($scope.timeout);
                }
            }

            function verificarSituacaoXml() {
                if ($scope.baixarXml && $scope.baixarXml.percentual < 100) {
                    $scope.timeout = $timeout(function () {
                        $scope.consultarXml();
                    }, 5000)
                }
            }

            $scope.$on("$destroy", function (event) {
                $timeout.cancel($scope.timeout);
            });

            $scope.handleTipoData = function () {
                $scope.dataInicial = null;
                $scope.dataFinal = null;
                $scope.competenciaAno = null;
                $scope.competenciaMes = null;

                if ($scope.tipoData == "EMISSAO") {
                    var hoje = new Date();
                    var month = hoje.getMonth();
                    var fullYear = hoje.getFullYear();
                    $scope.dataInicial = new Date(fullYear, month, 1);
                    $scope.dataFinal = hoje;
                } else {
                    var dataAtual = new Date();
                    $scope.competenciaAno = dataAtual.getFullYear();
                    angular.forEach($scope.meses, function (mes) {
                        if (mes.numeroMes == dataAtual.getMonth() + 1) {
                            $scope.competenciaMes = mes;
                        }
                    });
                }
            };

            $scope.handleTipoData();
        });
})();
