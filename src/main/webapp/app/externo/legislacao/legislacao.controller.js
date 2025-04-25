(function () {
    'use strict';
    angular.module('nfseApp')
        .controller('LegislacaoController', function ($scope, LegislacaoService, $http, $sce) {

            $scope.legislacoes = [];
            $scope.tiposLegislacao = [];
            $scope.legislacoesPorTipo = {};
            $scope.legislacaoSelecionada = null;

            function buscarLegislacoesParaExibicao() {
                LegislacaoService.legislacoesParaExibicao(function (data) {
                    $scope.legislacoes = data;
                    $scope.tiposLegislacao = [];
                    $scope.legislacoes.map(function (legislacao, i) {
                        var contem = $scope.tiposLegislacao.find(function (tipo) {
                            return tipo.id == legislacao.tipoLegislacaoDTO.id;
                        });
                        if (!contem) {
                            $scope.tiposLegislacao.push(legislacao.tipoLegislacaoDTO);
                            $scope.legislacoesPorTipo[legislacao.tipoLegislacaoDTO.id] = [];
                        }
                        $scope.legislacoesPorTipo[legislacao.tipoLegislacaoDTO.id].push(legislacao);
                    });
                    console.log($scope.tiposLegislacao);
                    console.log($scope.legislacoesPorTipo);
                });
            }

            $scope.exibirLegislacoes = function (tipoLegislacao) {
                tipoLegislacao.exibir = !tipoLegislacao.exibir;
                $scope.legislacaoSelecionada = null;
                if (tipoLegislacao.exibir) {
                    if ($scope.legislacoesPorTipo[tipoLegislacao.id].length > 0) {
                        $scope.buscarArquivo($scope.legislacoesPorTipo[tipoLegislacao.id][0]);
                    }
                }
            }

            buscarLegislacoesParaExibicao();

            $scope.buscarArquivo = function (legislacao) {
                $scope.legislacaoSelecionada = legislacao;
                $scope.conteudo = null;
                if (legislacao.arquivoDTO && legislacao.arquivoDTO.id) {
                    $http.get("/api/arquivo/" + legislacao.arquivoDTO.id, {responseType: 'arraybuffer'}).then(function (data) {
                        var arquivo = data;
                        var file = new Blob([arquivo.data], {type: 'application/pdf'});
                        var fileURL = window.URL.createObjectURL(file);
                        $scope.conteudo = $sce.trustAsResourceUrl(fileURL);
                    });
                }
            }

            $scope.isPdf = function () {
                if ($scope.legislacaoSelecionada &&
                    $scope.legislacaoSelecionada.arquivoDTO) {
                    return $scope.legislacaoSelecionada.arquivoDTO.nome.toLowerCase().endsWith('pdf');
                }
                return false;
            }

            $scope.baixarAnexo = function () {
                var a = document.createElement('a');
                a.href = $scope.fileURL;
                a.target = '_blank';
                a.download = 'legislacao.pdf';
                document.body.appendChild(a);
                a.click();
            }
        });
})();
