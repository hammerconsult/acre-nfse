(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('DebitosController', function ($scope, PrestadorServicos, ImpressaoPdf, $modal) {

            $scope.debitos = [];
            $scope.page = 1;
            $scope.searchQuery = "";
            $scope.per_page = 10;
            $scope.agrupador = {parcelas: []};
            $scope.hoje = new Date();
            $scope.situacoes = [
                {descricao: 'Em Aberto', value: 'EM_ABERTO'},
                {descricao: 'Pago', value: 'PAGO'},
                {descricao: 'Cancelado', value: 'CANCELAMENTO'},
                {descricao: 'Sem Movimento', value: 'SEM_MOVIMENTO'}
            ];

            $scope.filtro = {situacoes: []};
            $scope.filtro.situacoes.push($scope.situacoes[0]);

            $scope.loadAll = function () {
                PrestadorServicos.getDebitos(
                    {
                        vencimentoInicio: new Date($scope.filtro.vencimentoInicial),
                        vencimentoFim: new Date($scope.filtro.vencimentoFinal),
                        situacoes: $scope.filtro.situacoes.map(function (a) {
                            return a.value;
                        })
                    }, function (result, headers) {
                        // $scope.links = ParseLinks.parse(headers('link'));
                        $scope.debitos = result;
                    }
                )
                ;
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };
            $scope.loadAll();

            $scope.refresh = function () {
                $scope.loadAll();
            };

            $scope.imprimirDam = function () {
                var idsParcelas = [];
                for (var i = 0; i < $scope.agrupador.parcelas.length; i++) {
                    idsParcelas.push($scope.agrupador.parcelas[i].idParcela);
                }
                ImpressaoPdf.imprimirPdfViaPost('/api/imprimir-dam', idsParcelas);
            };

            $scope.adicionarParcela = function (servico) {
                $scope.agrupador.parcelas.push(servico);
            };

            $scope.removerParcela = function (index) {
                $scope.agrupador.parcelas.splice(index, 1);
            };

            $scope.enviarDam = function () {
                var modalEnviarNota = $modal.open({
                    templateUrl: 'app/entities/debitos/enviar-dam-email.html',
                    controller: 'EnviarDamEmailController',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return $scope.agrupador
                        }
                    }
                });
                modalEnviarNota.result.then(function (data) {
                });
            };

            $scope.visualizarGuia = function (idParcela) {
                $state.go("debitos.details", {id: idParcela});
            }

            $scope.isDebitoAbertoVencido = function (debito) {
                var vencimento = new Date(debito.vencimento);
                return vencimento < $scope.hoje && debito.situacao == 'EM_ABERTO';
            }
        });


    angular.module('nfseApp')
        .controller('EnviarDamEmailController', function ($scope, entity, PrestadorServicos, ParseLinks, SweetAlert,
                                                          $modalInstance, $state) {
            $scope.agrupador = entity;

            $scope.ok = function () {
                PrestadorServicos.enviarDamDaParcela($scope.agrupador, function (data) {
                    $modalInstance.close();
                    SweetAlert.swal("Operação realizada", "Email enviado com sucesso”", "info");
                }, function (err) {
                    SweetAlert.swal("Operação não realizada", "não foi possível enviar o email aos destinatários", "error");
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        });
})();
