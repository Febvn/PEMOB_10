package com.example.myfirstkmpapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette

@Composable
fun BottomNavBar(
    tabNavigator: TabNavigator,
    tabs: List<Tab>
) {
    val palette = LocalSkeuPalette.current

    Surface(
        color = palette.background,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = palette.shadowDark,
                spotColor = palette.shadowLight
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        palette.surface,
                        palette.surfaceVariant.copy(alpha = 0.5f)
                    )
                )
            )
            .height(72.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = tabNavigator.current == tab
                
                NavBarItem(
                    tab = tab,
                    isSelected = isSelected,
                    onClick = { tabNavigator.current = tab }
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    tab: Tab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val palette = LocalSkeuPalette.current
    
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) palette.primary else palette.onSurfaceLight,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val indicatorOffset by animateDpAsState(
        targetValue = if (isSelected) (-2).dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Column(
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .offset(y = indicatorOffset),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        tab.options.icon?.let {
            Icon(
                painter = it,
                contentDescription = tab.options.title,
                tint = contentColor,
                modifier = Modifier.size(26.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = tab.options.title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                color = contentColor
            )
        )
        
        // Underline effect
        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(4.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(palette.primary)
            )
        }
    }
}
