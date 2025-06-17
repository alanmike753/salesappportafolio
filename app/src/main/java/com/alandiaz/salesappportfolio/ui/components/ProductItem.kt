package com.alandiaz.salesappportfolio.ui.components

import androidx.compose.foundation.Image // <-- NUEVA IMPORTACIÓN
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale // <-- NUEVA IMPORTACIÓN
import androidx.compose.ui.res.painterResource // <-- NUEVA IMPORTACIÓN
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alandiaz.salesappportfolio.R // <-- IMPORTACIÓN PARA ACCEDER A R.drawable
import com.alandiaz.salesappportfolio.data.local.ProductEntity
import com.alandiaz.salesappportfolio.ui.theme.SalesAppPortfolioTheme

@Composable
fun ProductItem(
    product: ProductEntity,
    onItemClick: (productId: Int) -> Unit,
    onAddToCartClick: (productId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.clickable { onItemClick(product.productId) }
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // CAMBIO: Reemplazado Box con Image para mostrar la imagen local
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = "Imagen de ${product.name}",
                    modifier = Modifier
                        .size(80.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop // Asegura que la imagen se ajuste bien
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            // Botón para añadir al carrito (se mantiene igual)
            Button(
                onClick = { onAddToCartClick(product.productId) },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Añadir al carrito",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Añadir")
            }
        }
    }
}

// Preview actualizada para usar imageResId
@Preview(showBackground = true, name = "Preview Producto 1")
@Composable
fun ProductItemPlusNpkPreview() {
    SalesAppPortfolioTheme {
        ProductItem(
            product = ProductEntity(
                productId = 1,
                name = "Plus NPK",
                description = "Fertilizante balanceado para un crecimiento óptimo.",
                price = 100.00,
                imageResId = R.drawable.plusnpk,
                category = "Fertilizantes",
                stock = 100
            ),
            onItemClick = { productId -> println("Preview: Clic para detalle del producto ID: $productId") },
            onAddToCartClick = { productId -> println("Preview: Añadir al carrito producto ID: $productId") }
        )
    }
}

@Preview(showBackground = true, name = "Preview Producto 2")
@Composable
fun ProductItemIndazplusPreview() {
    SalesAppPortfolioTheme {
        ProductItem(
            product = ProductEntity(
                productId = 2,
                name = "Indazplus",
                description = "Potenciador de floración y cuajado de frutos.",
                price = 100.00,
                imageResId = R.drawable.indazplus,
                category = "Bioestimulantes",
                stock = 75
            ),
            onItemClick = { productId -> println("Preview: Clic para detalle del producto ID: $productId") },
            onAddToCartClick = { productId -> println("Preview: Añadir al carrito producto ID: $productId") }
        )
    }
}