(function () {
'use strict';

angular.module('nfseApp')
    .controller('ResetFinishController', function ($scope, $stateParams, $timeout, Auth, User) {

        $scope.keyMissing = $stateParams.key === undefined;
        $scope.doNotMatch = null;
        $scope.error = null;
        $scope.success = null;
        $scope.user = null;

        $scope.init = function () {
            $scope.invalidKey = $stateParams.key === undefined;
            if (!$scope.keyMissing) {
                User.fromResetKey({resetKey: $stateParams.key}, function (data) {
                    $scope.user = data;
                    if ($scope.user.id) {
                        $scope.resetAccount = {};
                        $timeout(function (){angular.element('[ng-model="resetAccount.password"]').focus();});
                    } else {
                        $scope.error = 'ERROR';
                    }
                });
            }
        }

        $scope.init();

        $scope.finishReset = function() {
            if ($scope.resetAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                Auth.resetPasswordFinish({key: $stateParams.key, newPassword: $scope.resetAccount.password}).then(function () {
                    $scope.success = 'OK';
                }).catch(function (response) {
                    $scope.success = null;

                });
            }

        };
    });
})();