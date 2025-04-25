(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('NotaFiscalAvulsaController',
            function ($rootScope, $scope, NotaFiscalAvulsa, ParseLinks, ImpressaoPdf, Account, DataUtil, Util) {

                $scope.notasFiscaisAvulsa = [];
                $scope.per_page = 10;
                $scope.searchQuery = "";
                $scope.usuario;
                $scope.mostrarFiltros = false;

                $scope.habilitarDesabilitarFiltros = function () {
                    $scope.mostrarFiltros = !$scope.mostrarFiltros;
                    $scope.iniciarFiltros();
                };

                $scope.iniciarFiltros = function () {
                    var fullYear = new Date().getFullYear();
                    $scope.emissaoInicial = new Date(fullYear - 6, 0, 1);
                    $scope.emissaoFinal = new Date(fullYear, 12, 31);
                    $scope.notaFiscalInicial = null;
                    $scope.notaFiscalFinal = null;
                    $scope.cpfCnpjTomador = null;
                    $scope.nomeRazaoSocialTomador = null;
                };

                $scope.iniciarFiltros();

                $scope.montarConsulta = function () {
                    var campos = [];
                    var parametroQueryCampo = {
                        campo: "nfsa.prestador_id",
                        operador: "IGUAL",
                        valorLong: $scope.usuario.pessoaId
                    };
                    campos.push(parametroQueryCampo);

                    if ($scope.emissaoInicial) {
                        var emissaoInicial = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoInicial);
                        parametroQueryCampo = {
                            campo: "trunc(nfsa.dataNota)",
                            operador: "MAIOR_IGUAL",
                            valorDate: emissaoInicial
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.emissaoFinal) {
                        var emissaoFinal = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoFinal);
                        parametroQueryCampo = {
                            campo: "trunc(nfsa.dataNota)",
                            operador: "MENOR_IGUAL",
                            valorDate: emissaoFinal
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.notaFiscalInicial) {
                        parametroQueryCampo = {
                            campo: "nfsa.numero",
                            operador: "MAIOR_IGUAL",
                            valorLong: $scope.notaFiscalInicial
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.notaFiscalFinal) {
                        parametroQueryCampo = {
                            campo: "nfsa.numero",
                            operador: "MENOR_IGUAL",
                            valorLong: $scope.notaFiscalFinal
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.cpfCnpjTomador) {
                        parametroQueryCampo = {
                            campo: "coalesce(pft.cpf, pjt.cnpj)",
                            operador: "LIKE",
                            valorString: "%" + Util.apenasNumeros($scope.cpfCnpjTomador) + "%"
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.nomeRazaoSocialTomador) {
                        parametroQueryCampo = {
                            campo: "lower(coalesce(pft.nome, pjt.razaosocial))",
                            operador: "LIKE",
                            valorString: "%" + $scope.nomeRazaoSocialTomador.toLowerCase() + "%"
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
                        orderBy: " order by nfsa.numero desc "
                    };
                };

                $scope.loadAll = function () {
                    var consulta = $scope.montarConsulta();
                    NotaFiscalAvulsa.search(consulta, function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.notasFiscaisAvulsa = result;
                    });
                };

                $scope.loadPage = function (page) {
                    $scope.page = page;
                    $scope.loadAll();
                };

                Account.get().$promise
                    .then(function (account) {
                        $scope.usuario = account.data;
                        console.log('usuario', $scope.usuario);
                        $scope.loadAll()
                    });

                $scope.imprimeNFSAvulsa = function (id) {
                    ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-nota-fiscal-avulsa/' + id);
                };
            }
        );
})();