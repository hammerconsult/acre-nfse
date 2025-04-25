(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('ArquivoDesifDetailController',
            function ($scope, $state, $timeout, arquivo, ParseLinks, ArquivoDesif, Notificacao,
                      ArquivoDesifRegistro0100, ArquivoDesifRegistro0200, ArquivoDesifRegistro0300,
                      ArquivoDesifRegistro0400, ArquivoDesifRegistro0410, ArquivoDesifRegistro0430,
                      ArquivoDesifRegistro0440, ArquivoDesifRegistro1000, ImpressaoPdf,
                      PlanoGeralContasComentado) {
                $scope.arquivo = arquivo;
                $scope.consultaRegistro0100 = {
                    page: 0,
                    per_page: 10,
                    links: '',
                    list: []
                };
                $scope.consultaRegistro0200 = {
                    page: 0,
                    per_page: 10,
                    links: '',
                    list: []
                };
                $scope.consultaRegistro0300 = {
                    page: 0,
                    per_page: 10,
                    links: '',
                    list: []
                };
                $scope.consultaRegistro0400 = {
                    page: 0,
                    per_page: 10,
                    links: '',
                    list: []
                };
                $scope.consultaRegistro0410 = {
                    page: 0,
                    per_page: 10,
                    links: '',
                    list: []
                };
                $scope.consultaRegistro0430 = {
                    page: 0,
                    per_page: 10,
                    links: '',
                    list: []
                };
                $scope.consultaRegistro0440 = {
                    page: 0,
                    per_page: 10,
                    links: '',
                    list: []
                };
                $scope.consultaRegistro1000 = {
                    page: 0,
                    per_page: 10,
                    links: '',
                    list: []
                };
                $scope.activeTab;
                $scope.timeout;
                $scope.contasTributaveisNaoDeclaradas = [];

                $scope.verificarAndamento = function () {
                    ArquivoDesif.get({id: $scope.arquivo.id}, function (arquivo) {
                        $scope.arquivo = arquivo;
                        if ($scope.arquivo.situacao == 'EM_PROCESSAMENTO') {
                            $scope.timeout = $timeout(function () {
                                $scope.verificarAndamento();
                            }, 5000);
                        }
                    });
                }

                $scope.verificarAndamento();

                $scope.buscarRegistros = function (service, consultaRegistro) {
                    var campos = [];
                    var parametroQueryCampo = {
                        campo: "obj.arquivodesif_id",
                        operador: "IGUAL",
                        valorLong: $scope.arquivo.id
                    };
                    campos.push(parametroQueryCampo);

                    var consultaGenerica = {
                        offset: consultaRegistro.page,
                        limit: consultaRegistro.per_page,
                        parametrosQuery: [{
                            juncao: " and ",
                            parametroQueryCampos: campos
                        }]
                    };

                    service.consultar(consultaGenerica,
                        function (result, headers) {
                            if (headers)
                                consultaRegistro.links = ParseLinks.parse(headers('link'));
                            consultaRegistro.list = result;
                        });
                }

                $scope.loadPageRegistros = function (page, service, consultaRegistro) {
                    consulta.page = page;
                    $scope.buscarRegistros(service, consultaRegistro);
                };

                $scope.loadPageRegistros0100 = function (page) {
                    $scope.loadPageRegistros(page, ArquivoDesifRegistro0100, $scope.consultaRegistro0100);
                };

                $scope.loadPageRegistros0200 = function (page) {
                    $scope.loadPageRegistros(page, ArquivoDesifRegistro0200, $scope.consultaRegistro0200);
                };

                $scope.loadPageRegistros0300 = function (page) {
                    $scope.loadPageRegistros(page, ArquivoDesifRegistro0300, $scope.consultaRegistro0300);
                };

                $scope.loadPageRegistros0400 = function (page) {
                    $scope.loadPageRegistros(page, ArquivoDesifRegistro0400, $scope.consultaRegistro0400);
                };

                $scope.loadPageRegistros0410 = function (page) {
                    $scope.loadPageRegistros(page, ArquivoDesifRegistro0410, $scope.consultaRegistro0410);
                };

                $scope.loadPageRegistros0430 = function (page) {
                    $scope.loadPageRegistros(page, ArquivoDesifRegistro0430, $scope.consultaRegistro0430);
                };

                $scope.loadPageRegistros0440 = function (page) {
                    $scope.loadPageRegistros(page, ArquivoDesifRegistro0430, $scope.consultaRegistro0440);
                };

                $scope.loadPageRegistros1000 = function (page) {
                    $scope.loadPageRegistros(page, ArquivoDesifRegistro1000, $scope.consultaRegistro1000);
                };

                $scope.buscarContasTributaveisNaoDeclaradas = function () {
                    PlanoGeralContasComentado.buscarContasTributaveisNaoDeclaradas({arquivo: $scope.arquivo.id}, function (data) {
                        $scope.contasTributaveisNaoDeclaradas = data;
                    });
                }

                $scope.defineActiveTab = function () {
                    switch ($scope.arquivo.modulo) {
                        case 'MODULO_1': {
                            $scope.activeTab = 3;
                            $scope.buscarRegistros(ArquivoDesifRegistro0400, $scope.consultaRegistro0400);
                            $scope.buscarRegistros(ArquivoDesifRegistro0410, $scope.consultaRegistro0410);
                            $scope.buscarRegistros(ArquivoDesifRegistro1000, $scope.consultaRegistro1000);
                            break;
                        }
                        case 'MODULO_2': {
                            $scope.activeTab = 3;
                            $scope.buscarRegistros(ArquivoDesifRegistro0400, $scope.consultaRegistro0400);
                            $scope.buscarRegistros(ArquivoDesifRegistro0430, $scope.consultaRegistro0430);
                            $scope.buscarRegistros(ArquivoDesifRegistro0440, $scope.consultaRegistro0440);
                            $scope.buscarContasTributaveisNaoDeclaradas();
                            break;
                        }
                        case 'MODULO_3': {
                            $scope.activeTab = 0;
                            $scope.buscarRegistros(ArquivoDesifRegistro0100, $scope.consultaRegistro0100);
                            $scope.buscarRegistros(ArquivoDesifRegistro0200, $scope.consultaRegistro0200);
                            $scope.buscarRegistros(ArquivoDesifRegistro0300, $scope.consultaRegistro0300);
                            break;
                        }
                        case 'MODULO_4': {
                            $scope.activeTab = 7;
                            $scope.buscarRegistros(ArquivoDesifRegistro1000, $scope.consultaRegistro1000);
                            break;
                        }
                    }
                }

                $scope.defineActiveTab();

                $scope.mudarActiveTab = function (activeTab) {
                    $scope.activeTab = activeTab;
                }

                $scope.excluir = function () {
                    Notificacao.confirmDelete(
                        function () {
                            ArquivoDesif.remove({id: $scope.arquivo.id}, function (data) {
                                $state.go('arquivoDesif');
                            })
                        }
                    );
                }

                $scope.enviar = function () {
                    Notificacao.confirm('Atenção!', 'Tem certeza que deseja enviar esse arquivo?', 'warning',
                        function () {
                            ArquivoDesif.enviar({id: $scope.arquivo.id}, function () {
                                Notificacao.info('Informação!', 'Arquivo enviado para processamento.');
                                $state.go('arquivoDesif.detalhe', {id: $scope.arquivo.id}, {reload: true});
                            });
                        });
                }

                $scope.imprimirGuia = function () {
                    ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-dam-declaracao/' + $scope.arquivo.idDeclaracaoMensalServico);
                };

                $scope.$on('$destroy', function () {
                    if ($scope.timeout) {
                        $timeout.cancel($scope.timeout);
                    }
                });

                $scope.imprimirRecibo = function () {
                    ImpressaoPdf.imprimirPdfViaUrl('/api/arquivo-desif/imprimir-recibo/' + $scope.arquivo.id);
                };
            });
})
();
