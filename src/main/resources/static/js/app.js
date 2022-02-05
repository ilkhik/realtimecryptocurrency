class View {

    updatePrices(prices) {
        this.clearPrices();
        for (const price of prices) {
            this.addPrice(price);
        }
    }

    addPrice(price) {
        const clone = $("#price-row-template").clone();
        clone.attr("id", null);
        clone.attr("class", "price-row");
        clone.find(".price-row-exchange").text(price.exchange);
        clone.find(".price-row-price").text(price.amount.toFixed(2));
        $("#price-row-template").before(clone);
        clone.css("display", "table-row");
    }

    clearPrices() {
        $("#price-table").find(".price-row").each(function () {
            this.remove();
        });
    }

    updateTableHeader(sortBy, order) {
        if (sortBy === "exchange") {
            $("#exchange-sort-triangle").html(order === "up" ? "&#x25B2;" : "&#9660;");
            $("#price-sort-triangle").html("");
        } else {
            $("#exchange-sort-triangle").html("");
            $("#price-sort-triangle").html(order === "up" ? "&#x25B2;" : "&#9660;");
        }
    }
}

class Application {

    constructor() {
        this.view = new View();
        this.prices = [];
        this.sortBy = "exchange";
        this.order = "up";

        $(document).ready(() => {
            this.getPricesFromServer();
            setInterval(() => {
                this.getPricesFromServer();
            }, 5000);

            const pairSelectionHandler = () => {
                this.view.clearPrices();
                this.getPricesFromServer();
            };

            $("#currency").on("change", pairSelectionHandler);

            $("#base").on("change", pairSelectionHandler);

            $("#exchange-column").on("click", () => {
                if (this.sortBy === "exchange") {
                    this.changeOrder();
                } else {
                    this.changeSortBy();
                }
                this.sortPrices();
                this.updateView();
            });

            $("#price-column").on("click", () => {
                if (this.sortBy === "amount") {
                    this.changeOrder();
                } else {
                    this.changeSortBy();
                }
                this.sortPrices();
                this.updateView();
            });
        });
    }

    changeOrder() {
        this.order = this.order === "up" ? "down" : "up";
        this.view.updateTableHeader(this.sortBy, this.order);
    }

    changeSortBy() {
        this.sortBy = this.sortBy === "exchange" ? "amount" : "exchange";
        this.order = "up";
        this.view.updateTableHeader(this.sortBy, this.order);
    }

    updateView() {
        this.view.updatePrices(this.prices);
    }

    sortPrices() {
        const ord = this.order === "up" ? 1 : -1;
        this.prices.sort((a, b) => (a[this.sortBy] > b[this.sortBy]) ? ord : (a[this.sortBy] < b[this.sortBy]) ? -ord : 0);
    }

    getPricesFromServer() {
        const base = $('#base').val();
        const currency = $('#currency').val();
        $.ajax({
            type: "GET",
            url: `/api/pricesByPair?base=${base}&currency=${currency}`,
            success: (prices) => {
                this.prices = prices;
                this.sortPrices();
                this.updateView();
            }
        });
    }

}

const app = new Application();