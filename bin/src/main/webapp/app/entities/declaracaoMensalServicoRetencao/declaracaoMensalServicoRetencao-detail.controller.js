(function () {
    'use strict';

    angular.module('nfseApp').controller('DeclaracaoMensalServicoRetencaoDetailController',
        function ($scope, $state, entity, DeclaracaoMensalServico,
                  ImpressaoPdf, Notificacao, DocumentoFiscal, ParseLinks) {
            $scope.declaracaoMensalServico = entity;

            $scope.page = 0;
            $scope.per_page = 20;
            $scope.documentos = [];
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
                Notificacao.confirm('Atenção!', 'Tem certeza que deseja cancelar essa declaração!', 'warning',
                    function () {
                        DeclaracaoMensalServico.cancelar({id: $scope.declaracaoMensalServico.id}, function () {
                            Notificacao.info('Operação Realizada.', 'Declaração Mensal de Serviço Cancelada!');
                            $state.go('declaracaoMensalServicoRetencao.detail', {id: $scope.declaracaoMensalServico.id}, {reload: true});
                        });
                    })
            };
        });

})();
