(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('TomadorController', function ($scope, $filter, Tomador, TomadorSearch, ParseLinks, SweetAlert, ExportarXls,
                                                   localStorageService) {
            $scope.tomadors = [];
            $scope.page = 1;
            $scope.per_page = 10;
            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
            $scope.searchQuery;


            $scope.montarConsultaGenerica = function () {
                var parametrosQuery = [];
                var parametroQueryPrestador =
                    {
                        juncao: " and ",
                        parametroQueryCampos: [{
                            campo: "t.prestador_id",
                            operador: "IGUAL",
                            valorLong: $scope.prestadorPrincipal.prestador.id
                        }]
                    };
                parametrosQuery.push(parametroQueryPrestador);

                if ($scope.searchQuery) {
                    var parametroQueryFiltro =
                        {
                            juncao: " or ",
                            parametroQueryCampos: [
                                {
                                    campo: "dados.cpfcnpj",
                                    operador: "LIKE",
                                    valorString: "%" + $scope.searchQuery + "%"
                                },
                                {
                                    campo: "dados.nomerazaosocial",
                                    operador: "LIKE",
                                    valorString: "%" + $scope.searchQuery + "%"
                                },
                                {
                                    campo: "dados.nomefantasia",
                                    operador: "LIKE",
                                    valorString: "%" + $scope.searchQuery + "%"
                                },
                                {
                                    campo: "dados.apelido",
                                    operador: "LIKE",
                                    valorString: "%" + $scope.searchQuery + "%"
                                },
                                {
                                    campo: "dados.email",
                                    operador: "LIKE",
                                    valorString: "%" + $scope.searchQuery + "%"
                                }
                            ]
                        };
                    parametrosQuery.push(parametroQueryFiltro);
                }

                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: parametrosQuery,
                    orderBy: ""
                };
            };

            $scope.loadAll = function () {
                var consultaGenerica = $scope.montarConsultaGenerica();
                Tomador.query(consultaGenerica,
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.tomadors = result;
                    });
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.loadAll();

            $scope.delete = function (id) {
                Tomador.get({id: id}, function (result) {
                    $scope.tomador = result;

                    SweetAlert.swal({
                            title: "Confirme a exclusão",
                            text: "Você tem certeza que quer excluir o Tomador?",
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Excluir",
                            cancelButtonText: "Não, Cancelar",
                            closeOnConfirm: false,
                            closeOnCancel: false
                        },
                        function (isConfirm) {
                            if (isConfirm) {
                                $scope.confirmDelete(id);
                            } else {
                                SweetAlert.swal("Cancelado", "O Tomador não foi removido :)", "error");
                            }
                        });

                });
            };

            $scope.confirmDelete = function (id) {
                Tomador.delete({id: id},
                    function () {
                        $scope.loadAll();
                        SweetAlert.swal("Removido!", "O Tomador foi removido com sucesso.", "success");
                    });
            };

            var colunas = [
                {descricao: 'Apelido', valor: 'apelido'},
                {descricao: 'Cpf/Cnpj', valor: 'cpfCnpj'},
                {descricao: 'Nome/Razão Social', valor: 'nomeRazaoSocial'},
                {descricao: 'Email', valor: 'email'},
            ];

            function getLinhas() {
                var linhas = [];
                for (var i = 0; i < $scope.tomadors.length; i++) {
                    var tomador = $scope.tomadors[i];
                    tomador.apelido =
                        linhas.push({
                            apelido: tomador.dadosPessoais.apelido,
                            cpfCnpj: tomador.dadosPessoais.cpfCnpj,
                            nomeRazaoSocial: tomador.dadosPessoais.nomeRazaoSocial,
                            email: tomador.dadosPessoais.email
                        });
                }
                return linhas;
            }

            $scope.baixarXls = function () {
                var xls = {
                    colunas: colunas,
                    linhas: getLinhas()
                };
                ExportarXls.getXls(xls);
            };

            $scope.baixarPDF = function () {
                var xls = {
                    colunas: colunas,
                    linhas: getLinhas()
                };
                ExportarXls.getPdf(xls);
            };
        });
})();
