package com.franquias.api.controllers;

import com.franquias.api.security.AutorizacaoUnidadeUtil;
import com.franquias.api.services.EstoqueService;
import com.franquias.api.dtos.EstoqueResponse;
import com.franquias.api.services.RelatorioService;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioController {

    private final RelatorioService service = new RelatorioService();
    private final EstoqueService estoqueService = new EstoqueService();

    public void faturamentoPorUnidade(Context ctx) {
        Long unidadeId = resolverUnidadeId(ctx);
        LocalDate[] periodo = lerPeriodo(ctx);
        ctx.json(service.faturamentoPorUnidade(unidadeId, periodo[0], periodo[1]));
    }

    public void rankingUnidades(Context ctx) {
        LocalDate[] periodo = lerPeriodo(ctx);
        Integer limite = lerLimite(ctx);
        ctx.json(service.rankingUnidades(periodo[0], periodo[1], limite));
    }

    public void royaltiesTotais(Context ctx) {
        Long unidadeId = resolverUnidadeId(ctx);
        String periodo = ctx.queryParam("periodo");
        ctx.json(service.royaltiesTotais(unidadeId, periodo));
    }

    public void produtosMaisVendidos(Context ctx) {
        Long unidadeId = resolverUnidadeId(ctx);
        LocalDate[] periodo = lerPeriodo(ctx);
        Integer limite = lerLimite(ctx);
        ctx.json(service.produtosMaisVendidos(unidadeId, periodo[0], periodo[1], limite));
    }

    public void estoqueCritico(Context ctx) {
        Long unidadeId = resolverUnidadeId(ctx);
        List<EstoqueResponse> lista = estoqueService.buscar(unidadeId, null, true).stream()
                .map(EstoqueResponse::fromEntity)
                .collect(Collectors.toList());
        ctx.json(lista);
    }

    public void chamadosPorStatus(Context ctx) {
        Long unidadeId = resolverUnidadeId(ctx);
        ctx.json(service.chamadosPorStatus(unidadeId));
    }

    // ---------- helpers ----------

    // ADMIN pode filtrar por unidade ou ver a rede toda; demais perfis são restritos à própria
    private Long resolverUnidadeId(Context ctx) {
        if (AutorizacaoUnidadeUtil.isAdmin(ctx)) {
            String param = ctx.queryParam("unidadeId");
            return param != null && !param.isBlank() ? Long.valueOf(param) : null;
        }
        return AutorizacaoUnidadeUtil.unidadeIdDoToken(ctx);
    }

    private LocalDate[] lerPeriodo(Context ctx) {
        String inicioParam = ctx.queryParam("dataInicio");
        String fimParam = ctx.queryParam("dataFim");
        LocalDate inicio = inicioParam != null && !inicioParam.isBlank() ? LocalDate.parse(inicioParam) : null;
        LocalDate fim = fimParam != null && !fimParam.isBlank() ? LocalDate.parse(fimParam) : null;
        return new LocalDate[]{inicio, fim};
    }

    private Integer lerLimite(Context ctx) {
        String param = ctx.queryParam("limite");
        return param != null && !param.isBlank() ? Integer.valueOf(param) : null;
    }
}
