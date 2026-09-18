package com.franquias.api.dtos;

import java.math.BigDecimal;

public class RoyaltiesTotaisResponse {

    private BigDecimal totalGerado;
    private BigDecimal totalPago;
    private BigDecimal totalPendente;
    private BigDecimal totalAtrasado;

    public RoyaltiesTotaisResponse(BigDecimal totalGerado, BigDecimal totalPago,
                                    BigDecimal totalPendente, BigDecimal totalAtrasado) {
        this.totalGerado = totalGerado;
        this.totalPago = totalPago;
        this.totalPendente = totalPendente;
        this.totalAtrasado = totalAtrasado;
    }

    public BigDecimal getTotalGerado() { return totalGerado; }
    public BigDecimal getTotalPago() { return totalPago; }
    public BigDecimal getTotalPendente() { return totalPendente; }
    public BigDecimal getTotalAtrasado() { return totalAtrasado; }
}
