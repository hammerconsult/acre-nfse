(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('NotaFiscal', function ($resource, DateUtils, SweetAlert, $state, $modal, ImpressaoPdf) {
            var resource = $resource('api/notaFiscals/:id', {}, {
                'dataGet': {
                    url: '/api/notaFiscals/get',
                    method: 'GET'
                },
                'dataPost': {
                    url: '/api/notaFiscals/post',
                    method: 'POST'
                },
                'query': {method: 'GET', isArray: true},
                'getPublica': {
                    url: '/api/publica/nota-fiscal/:id',
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'buscarCancelamentosNaoVisualizados': {
                    url: '/api/notaFiscals/cancelamentos-nao-visualizados',
                    method: 'GET',
                    isArray: true
                },
                'removerDoMongo': {
                    url: '/api/publico/nota-fiscal/remove-mongo/:id',
                    method: 'DELETE',
                },
                'buscarNotasComoTomador': {
                    method: 'GET',
                    url: 'api/notaFiscals/notas-tomadas',
                    isArray: true
                },
                'buscarNotasComoTomadorAndLote': {
                    method: 'GET',
                    url: 'api/notaFiscals/notas-tomadas-por-lote',
                    isArray: true
                },
                'buscarNotasFiscais': {
                    method: 'POST',
                    url: 'api/buscar-notas-fiscais',
                    isArray: true
                },
                'buscarCancelamentos': {
                    method: 'POST',
                    url: 'api/buscar-cancelamentos',
                    isArray: true
                },
                'buscarUltimasDezNotasFiscais': {
                    method: 'POST',
                    url: 'api/buscar-ultimas-dez-notas-fiscais',
                    isArray: true
                },
                'getRecebidasComFiltros': {
                    method: 'POST',
                    url: 'api/buscar-recebidas-filtro',
                    isArray: true
                },
                'new': {
                    method: 'GET',
                    url: 'api/notaFiscals/new'
                },
                'newRpsManual': {
                    method: 'GET',
                    url: 'api/notaFiscals/newRpsManual'
                },
                'getUltimoNumeroRps': {
                    method: 'GET',
                    url: 'api/notaFiscals/ultimo-numero-rps'
                },
                'cancelar': {
                    method: 'POST',
                    url: 'api/notaFiscals/cancelar',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'marcarCancelamentoVisualizado': {
                    method: 'PUT',
                    url: 'api/notaFiscals/marcar-cancelamento-visualizado',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'cartaCorrecao': {
                    method: 'POST',
                    url: 'api/notaFiscals/carta-correcao'
                },
                'submeterCancelamento': {
                    method: 'POST',
                    url: 'api/notaFiscals/submeter-cancelamento'
                },
                'imprimirCartaCorrecao': {
                    method: 'GET',
                    url: 'api/notaFiscals/carta-correcao-impressao',
                    headers: {
                        accept: 'application/pdf'
                    }
                },
                'buscarCartaCorrecao': {
                    method: 'GET',
                    isArray: true,
                    url: 'api/notaFiscals/carta-correcao-por-nota/:idNotaFiscal'
                },
                'getCancelamentos': {
                    method: 'GET',
                    isArray: true,
                    url: 'api/notaFiscals/cancelamentos/:id'
                },
                'autenticar': {
                    method: 'POST',
                    url: 'api/notaFiscals/autenticar',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'copy': {
                    method: 'GET',
                    url: 'api/notaFiscals/copy/:id',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'copyPorRps': {
                    method: 'GET',
                    url: 'api/notaFiscals/copy-rps/:numeroRps',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'getDiscriminacoesPorPrestador': {
                    method: 'GET',
                    isArray: true,
                    url: "api/notaFiscals/discriminacoes"
                },
                'getTomadorPorCpfCnpj': {
                    method: 'GET',
                    url: "api/tomador_da_nota_por_cpfCnpj/:cpfCnpj",
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'getEmitidasPorMes': {
                    url: "/api/nota_emita_por_mes",
                    method: 'GET',
                    isArray: true
                },
                'imprime': {
                    method: 'GET',
                    url: "/publico/notaFiscal/imprime/:id",
                    headers: {
                        accept: 'application/pdf'
                    },
                    responseType: 'arraybuffer',
                    cache: true,
                },
                'imprimeRelatorio': {
                    method: 'POST',
                    url: "/api/imprimir-relatorio-notas-fiscais",
                    headers: {
                        accept: 'application/pdf'
                    },
                    responseType: 'arraybuffer',
                    cache: true,
                },
                'update': {
                    method: 'PUT'
                },
                'enviarPorEmail': {
                    url: "/api/enviar-nota-fiscal",
                    method: 'PUT'
                },
                'baixarXml': {
                    method: 'POST',
                    url: '/api/notaFiscals/baixar-xml'
                },
                'consultarXml': {
                    method: 'POST',
                    url: '/api/notaFiscals/consultar-xml'
                },
                'baixarXmlNotaFiscal': {
                    method: 'GET',
                    url: '/api/notaFiscals/baixar-xml-nota-fiscal'
                },
                'baixarXls': {
                    method: 'GET',
                    url: '/api/notaFiscals/baixar-xls'
                },
                'getParcelasDaNota': {
                    method: 'GET',
                    url: 'api/notaFiscals/parcelas/:id',
                    isArray: true,
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'getPorCalculo': {
                    method: 'GET',
                    url: 'api/notaFiscals/por-calculo',
                    isArray: true,
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'imprimirRelatorioNotasFiscais': {
                    method: 'POST',
                    url: 'api/notaFiscals/relatorio-notas-fiscais',
                    headers: {
                        accept: 'application/pdf'
                    }
                },
                'buscarNotasSemDeclararPorCompetencia': {
                    method: 'GET',
                    url: 'api/notas-sem-declarar-por-competencia',
                    isArray: true
                },
                'buscarResumoSemDeclararPorCompetencia': {
                    method: 'GET',
                    url: 'api/resumo-sem-declarar-por-competencia'
                },
                'porNumero': {
                    method: 'GET',
                    url: 'api/notaFiscal-por-numero'
                },
                'calcularValores': {
                    method: 'POST',
                    url: 'api/nota-fiscal/calcular-valores'
                },
                'buscarPlacar': {
                    method: 'GET',
                    url: 'api/externo/placar'
                },
                'cacheGeracaoXml': {
                    method: 'GET',
                    url: 'api/notaFiscals/cache-geracao-xml',
                    isArray: true
                },
                'removeCacheGeracaoXml': {
                    method: 'DELETE',
                    url: 'api/notaFiscals/remove-cache-geracao-xml'
                },
                'integrarNotaNacional': {
                    method: 'GET',
                    url: 'api/notaFiscals/integrarNotaNacional'
                },
            });

            resource.copiarNota = function (nota) {
                SweetAlert.swal({
                        title: "Confirme a cópia da Nota Fiscal",
                        text: "Tem certeza que quer copiar a Nota Fiscal de N° " + nota.numero + " para a competência atual? ",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#F7A54A", confirmButtonText: "Sim, Copiar Nota Fiscal",
                        cancelButtonText: "Não, Cancelar",
                        closeOnConfirm: false,
                        closeOnCancel: false
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            $state.go('notaFiscal.copy', ({id: nota.id}));
                            SweetAlert.swal("Nota Fiscal Copiada!", "Prossiga para a emissão da Nota Fiscal.", "success");
                        } else {
                            SweetAlert.swal("Cancelado", "A Nota Fiscal não foi copiada.", "error");
                        }
                        SweetAlert.close();
                    }
                )
            };

            resource.enviarNota = function (nota) {
                var modalEnviarNota = $modal.open({
                    templateUrl: 'app/entities/notaFiscal/enviar-nota-email.html',
                    controller: 'EnviarNotaEmailController',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return nota
                        }
                    }
                });
                modalEnviarNota.result.then(function (data) {
                    if (data) {
                    }
                });
            };

            resource.imprimirNota = function (id) {
                ImpressaoPdf.imprimirPdfViaUrl('/publico/notaFiscal/imprime/' + id);
            };

            resource.cancelarNota = function (nota, onSucess) {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/notaFiscal/cancelamentoNotaFiscal.html',
                    controller: 'CancelamentoNotaFiscalController',
                    size: 'lg',
                    resolve: {
                        entity: function (NotaFiscal) {
                            return NotaFiscal.get({id: nota.id}).$promise;
                        },
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('motivoCancelamento');
                            $translatePartialLoader.addPart('situacaoDeferimento');
                            return $translate.refresh();
                        }]
                    }
                });
                modalInstance.result.then(function (notaFiscal) {
                    console.log(notaFiscal);
                    if (notaFiscal.situacao === 'CANCELADA') {
                        SweetAlert.success("Operação realizada", "Nota Fiscal Cancelada!");
                    } else {
                        SweetAlert.warning("Solicitação em análise", "Assim que aprovada sua solicitação o documento será cancelado!");
                    }
                    onSucess();
                }, function () {
                });
            };

            resource.cartaCorrecaoNota = function (nota, onSucess) {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/notaFiscal/carta-correcao/carta-correcao.html',
                    controller: 'CartaCorrecaoController',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return nota
                        }
                    }
                });
                modalInstance.result.then(function (notaFiscal) {
                    SweetAlert.success("Operação realizada", "Carta de Correção enviada!");
                    onSucess();
                });
            };

            resource.downloadXmlNotaFiscal = function (idNotaFiscal) {
                resource.baixarXmlNotaFiscal({
                    idNotaFiscal: idNotaFiscal
                }, function (result) {
                    var a = document.createElement('a');
                    a.href = 'data:application/xml;base64,' + result.conteudo;
                    a.target = '_blank';
                    a.download = 'notaFiscal' + idNotaFiscal + '.xml';
                    document.body.appendChild(a);
                    a.click();
                });
            };

            return resource;
        })
    ;
})();
