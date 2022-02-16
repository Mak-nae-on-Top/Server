package com.maknaeontop.location;
/*
/* 칼만필터 사용의 경우, 모델을 따로 측정하지 않고 바로 값을 결정. 추정값을 따로 적용 못할 수 도 있음.
* KalmanFilter code : https://github.com/fgroch/beacon-rssi-resolver/blob/master/src/main/java/tools/blocks/filter/
* 5. 측정값으로 추정치 조정 : https://gaussian37.github.io/ad-ose-lkf_basic/
* http://www.aistudy.com/control/kalman_filter.htm

public class KalmanFilter{

    private double processNoise;//Process noise
    private double measurementNoise;//Measurement noise
    private double estimatedRSSI;//calculated rssi
    private double errorCovarianceRSSI;//calculated covariance
    private boolean isInitialized = false;//initialization flag

    public KalmanFilter() {
        this.processNoise =0.125;
        this.measurementNoise = 0.8;
    }

    public KalmanFilter(double processNoise, double measurementNoise) {
        this.processNoise = processNoise;
        this.measurementNoise = measurementNoise;
    }

    @Override
    public double applyFilter(double rssi) {
        double priorRSSI;
        double kalmanGain;
        double priorErrorCovarianceRSSI;
        if (!isInitialized) {
            priorRSSI = rssi;
            priorErrorCovarianceRSSI = 1;
            isInitialized = true;
        } else {
            priorRSSI = estimatedRSSI;
            priorErrorCovarianceRSSI = errorCovarianceRSSI + processNoise;
        }

        kalmanGain = priorErrorCovarianceRSSI / (priorErrorCovarianceRSSI + measurementNoise);
        estimatedRSSI = priorRSSI + (kalmanGain * (rssi - priorRSSI));
        errorCovarianceRSSI = (1 - kalmanGain) * priorErrorCovarianceRSSI;

        return estimatedRSSI;
    }
}
 */
