(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('MetricsController', function ($scope, MonitoringService) {
            $scope.metrics = {};

            function getStatistic(measurements, statistic) {
                for (var i = 0; i < measurements.length; i++) {
                    if (measurements[i].statistic == statistic) {
                        return measurements[i].value;
                    }
                }
                return null;
            }

            function getStatisticValue(measurements) {
                return getStatistic(measurements, 'VALUE');
            }

            function getStatisticCount(measurements) {
                return getStatistic(measurements, 'COUNT');
            }

            function getStatisticTotalTime(measurements) {
                return getStatistic(measurements, 'TOTAL_TIME');
            }

            function getStatisticMax(measurements) {
                return getStatistic(measurements, 'MAX');
            }

            $scope.refreshMetricsJvm = function () {
                $scope.metrics.jvm = {
                    memory: {
                        max: 0,
                        used: 0,
                        heap: {
                            max: 0,
                            used: 0
                        },
                        nonheap: {
                            max: 0,
                            used: 0
                        }
                    },
                    threads: {
                        count: 0,
                        runnable: 0,
                        new: 0,
                        timed_waiting: 0,
                        blocked: 0
                    },
                    gc: {
                        memory_promoted: 0,
                        memory_allocated: 0,
                        max_data_size: 0,
                        live_data_size: 0,
                        pause: {
                            g1_evacuation: 0,
                            metadata_gc_threshold: 0
                        }
                    }
                };
                MonitoringService.getMetric('jvm.memory.max').then(function (data) {
                    $scope.metrics.jvm.memory.max = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.memory.used').then(function (data) {
                    $scope.metrics.jvm.memory.used = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.memory.max?tag=area:heap').then(function (data) {
                    $scope.metrics.jvm.memory.heap.max = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.memory.used?tag=area:heap').then(function (data) {
                    $scope.metrics.jvm.memory.heap.used = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.memory.max?tag=area:nonheap').then(function (data) {
                    $scope.metrics.jvm.memory.nonheap.max = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.memory.used?tag=area:nonheap').then(function (data) {
                    $scope.metrics.jvm.memory.nonheap.used = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.threads.states').then(function (data) {
                    $scope.metrics.jvm.threads.count = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.threads.states?tag=state:runnable').then(function (data) {
                    $scope.metrics.jvm.threads.runnable = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.threads.states?tag=state:timed-waiting').then(function (data) {
                    $scope.metrics.jvm.threads.timed_waiting = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.threads.states?tag=state:new').then(function (data) {
                    $scope.metrics.jvm.threads.new = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.threads.states?tag=state:blocked').then(function (data) {
                    $scope.metrics.jvm.threads.blocked = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.gc.memory.promoted').then(function (data) {
                    $scope.metrics.jvm.gc.memory_promoted = getStatisticCount(data.measurements);
                });
                MonitoringService.getMetric('jvm.gc.memory.allocated').then(function (data) {
                    $scope.metrics.jvm.gc.memory_allocated = getStatisticCount(data.measurements);
                });
                MonitoringService.getMetric('jvm.gc.max.data.size').then(function (data) {
                    $scope.metrics.jvm.gc.max_data_size = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.gc.live.data.size').then(function (data) {
                    $scope.metrics.jvm.gc.live_data_size = getStatisticValue(data.measurements);
                });
                MonitoringService.getMetric('jvm.gc.pause?tag=cause:G1 Evacuation Pause').then(function (data) {
                    $scope.metrics.jvm.gc.pause.g1_evacuation = getStatisticTotalTime(data.measurements);
                });
                MonitoringService.getMetric('jvm.gc.pause?tag=cause:Metadata GC Threshold').then(function (data) {
                    $scope.metrics.jvm.gc.pause.metadata_gc_threshold = getStatisticTotalTime(data.measurements);
                });
            }

            $scope.refreshMetricsHttpServerRequest = function () {
                $scope.metrics.http_server_request = {
                    count: 0,
                    total_time: 0,
                    max: 0
                }
                MonitoringService.getMetric('http.server.requests').then(function (data) {
                    $scope.metrics.http_server_request.count = getStatisticCount(data.measurements);
                    $scope.metrics.http_server_request.total_time = getStatisticTotalTime(data.measurements);
                    $scope.metrics.http_server_request.max = getStatisticMax(data.measurements);
                });
            }

            $scope.refresh = function () {
                $scope.refreshMetricsJvm();
                $scope.refreshMetricsHttpServerRequest();
            };

            $scope.refresh();
            
        });
})();