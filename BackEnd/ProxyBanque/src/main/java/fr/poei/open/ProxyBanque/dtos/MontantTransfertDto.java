package fr.poei.open.proxybanque.dtos;

public class MontantTransfertDto {

    private Long montant;


    public MontantTransfertDto() {
        super();
    }


    public MontantTransfertDto(Long montant) {
        super();
        this.montant = montant;
    }


    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }


    @Override
    public String toString() {
        return "MontantTransfertDto [montant=" + montant + "]";
    }
}