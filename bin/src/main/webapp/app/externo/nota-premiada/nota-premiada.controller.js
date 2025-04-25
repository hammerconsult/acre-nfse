(function () {
    'use strict';

angular.module('nfseApp')
    .controller('NotaPremiadaController', function ($scope, NotaPremiadaService, $modal, SweetAlert, Principal) {

        $scope.notas = [];
        $scope.premios = [];
        $scope.account = {};

        function buscarUsuarioLogado() {
            Principal.identity().then(function (account) {
                if (account) {
                    $scope.account = account;
                }
            });
        }

        buscarUsuarioLogado();

        function buscarNotaPremiadaParaExibicao() {
            NotaPremiadaService.notaPremiadaParaExibicao(function (data) {
                $scope.notas = data;
            });
        }

        buscarNotaPremiadaParaExibicao();

        function buscarTodosPremios() {
            NotaPremiadaService.buscarTodosPremios(function (data) {
                $scope.premios = data;
            });
        }

        buscarTodosPremios();


        $scope.verDetalhes = function (nota) {
            var modalInstance = $modal.open({
                templateUrl: 'app/externo/nota-premiada/nota-premiada-detalhes.html',
                controller: 'NotaPremiadaDetalhesController',
                size: 'lg',
                resolve: {
                    nota: function () {
                        return nota;
                    }
                }
            });

        };

        $scope.verBilhetes = function (nota) {
            var modalInstance = $modal.open({
                templateUrl: 'app/externo/nota-premiada/nota-premiada-detalhes-bilhete.html',
                controller: 'NotaPremiadaDetalhesBilheteController',
                size: 'lg',
                resolve: {
                    nota: function () {
                        return nota;
                    },

                }
            });
        };

        $scope.anularParticipacaoCampanha = function (nota) {
            SweetAlert.swal({
                    title: "Confirme a operação",
                    text: "Você tem certeza que não quer participar dessa campanha?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Não quero participar!",
                    cancelButtonText: "Não, Cancelar",
                    closeOnConfirm: false,
                    closeOnCancel: false
                },
                function (isConfirm) {
                    if (isConfirm) {
                        anularParticipacao(nota);
                    } else {
                        SweetAlert.swal("Cancelado", "Operação Cancelada", "error");
                    }
                });
        };

        function anularParticipacao(nota) {
            var notaPremiadaPessoa = {
                login: $scope.account.login,
                id: nota.id
            };
            NotaPremiadaService.anularParticipacaoCampanha(notaPremiadaPessoa, function () {
                SweetAlert.swal("Tudo Certo!", "Participação na campanha cancelada!", "success");
            });
        }

        $scope.verBilhetesPremiados = function (nota) {
            var modalInstance = $modal.open({
                templateUrl: 'app/externo/nota-premiada/nota-premiada-detalhes-bilhete-premiado.html',
                controller: 'NotaPremiadaDetalhesBilhetePremiadoController',
                size: 'lg',
                resolve: {
                    nota: function () {
                        return nota;
                    }
                }
            });

        };

    });

angular.module('nfseApp')
    .controller('NotaPremiadaDetalhesController', function ($scope, $modalInstance, nota, NotaPremiadaService) {

        $scope.nota = nota;

        $scope.premios = [];

        function buscarPremios() {
            NotaPremiadaService.buscarPremios(
                {id: $scope.nota.id}, function (result) {
                    $scope.premios = result;
                }
            );
        }

        buscarPremios();

        $scope.ok = function (nota) {
            $modalInstance.close(nota);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    });

angular.module('nfseApp')
    .controller('NotaPremiadaDetalhesBilheteController', function ($scope, $modalInstance, nota, NotaPremiadaService, Principal) {

        $scope.nota = nota;

        $scope.bilhetes = [];

        $scope.account = {};

        function buscarUsuarioLogado() {
            Principal.identity().then(function (account) {
                if (account) {
                    $scope.account = account;
                    buscarBilhetes();
                }
            });
        }

        buscarUsuarioLogado();

        function buscarBilhetes() {
            NotaPremiadaService.buscarBilhetes(
                {
                    login: $scope.account.login,
                    id: nota.id
                }, function (result) {
                    $scope.bilhetes = result;
                }
            );
        }


        $scope.ok = function (nota) {
            $modalInstance.close(nota);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    });

angular.module('nfseApp')
    .controller('NotaPremiadaDetalhesBilhetePremiadoController', function ($scope, $modalInstance, nota, NotaPremiadaService) {

        $scope.nota = nota;

        $scope.bilhetes = [];

        function buscarBilhetesPremiados() {
            NotaPremiadaService.buscarBilhetesPremiados(
                {id: $scope.nota.id}, function (result) {
                    $scope.bilhetes = result;
                }
            );
        }

        buscarBilhetesPremiados();

        $scope.ok = function (nota) {
            $modalInstance.close(nota);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    });
})();
