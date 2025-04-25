(function () {
    'use strict';

    angular.module('nfseApp').controller('DeclaracaoMensalServicoDetailController',
        function ($scope, $state, entity, DeclaracaoMensalServico, ImpressaoPdf,
                  Notificacao, DocumentoFiscal, ParseLinks) {
            $scope.declaracaoMensalServico = entity;
            $scope.documentos = [];
            $scope.quantidadeNotasFiscaisNaDeclaracao = 0;
            $scope.valorTotalServicos = 0;
            $scope.valorTotalIss = 0;
            $scope.valorTotalIssRetido = 0;
            $scope.page = 0;
            $scope.per_page = 20;
            $scope.buscarDocumentos = buscarDocumentos;

            function montarFiltrosDocumentos() {
                var campos = [];
                var parametroQueryCampo = {
                    campo: " exists (select 1 from notadeclarada nd " +
                        " where nd.declaracaomensalservico_id = " + $scope.declaracaoMensalServico.id +
                        "  and nd.declaracaoprestacaoservico_id = obj.id) "
                };
                campos.push(parametroQueryCampo);


                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                };
            }

            function buscarDocumentos() {
                DocumentoFiscal.consultar(montarFiltrosDocumentos(),
                    function (result, headers) {
                        if (headers) {
                            $scope.links = ParseLinks.parse(headers('link'));
                            $scope.totalItens = headers('x-total-count');
                        }
                        $scope.documentos = result;
                        atualizarContadores();
                    });
            }

            function atualizarContadores() {
                $scope.agrupador = {};
                $scope.valorTotalServicos = 0;
                $scope.valorTotalIss = 0;
                $scope.valorTotalIssRetido = 0;

                angular.forEach($scope.documentos, function (documento) {
                    $scope.quantidadeNotasFiscaisNaDeclaracao += 1;
                    $scope.valorTotalServicos += documento.totalServicos;
                    $scope.valorTotalIss += documento.issPagar;
                    if (documento.issRetido) {
                        $scope.valorTotalIssRetido += documento.issCalculado;
                    }
                    if (!$scope.agrupador[documento.naturezaOperacao]) {
                        $scope.agrupador[documento.naturezaOperacao] = {
                            natureza: documento.naturezaOperacao,
                            quantidade: 0,
                            servicos: 0,
                            iss: 0
                        };
                    }
                    $scope.agrupador[documento.naturezaOperacao].quantidade += 1;
                    $scope.agrupador[documento.naturezaOperacao].servicos += documento.totalServicos;
                    $scope.agrupador[documento.naturezaOperacao].iss += documento.issCalculado;
                });
            }

            buscarDocumentos();

            $scope.imprimirDam = function () {
                ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-dam-declaracao/' + $scope.declaracaoMensalServico.id);
            };

            $scope.imprimeRelatorioDMS = function () {
                ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-relatorio-dms/' + $scope.declaracaoMensalServico.id);
            };

            $scope.cancelar = function () {
                Notificacao.confirm('Atenção!', 'Tem certeza que deseja cancelar essa declaração?', 'warning',
                    function () {
                        DeclaracaoMensalServico.cancelar({id: $scope.declaracaoMensalServico.id}, function () {
                            Notificacao.info('Operação Realizada.', 'Declaração Mensal de Serviço Cancelada!');
                            $state.go('declaracaoMensalServico.detail', {id: $scope.declaracaoMensalServico.id}, {reload: true});
                        });
                    })
            };
        });

})();
