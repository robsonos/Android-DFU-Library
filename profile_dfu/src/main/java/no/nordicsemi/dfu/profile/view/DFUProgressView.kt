package no.nordicsemi.dfu.profile.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.nordicsemi.dfu.profile.R
import no.nordicsemi.ui.scanner.ui.exhaustive

@Composable
internal fun DFUProgressView(viewEntity: DFUProgressViewEntity, onEvent: (DFUViewEvent) -> Unit) {
    when (viewEntity) {
        DisabledProgressViewEntity -> DisabledDFUProgressView()
        is WorkingProgressViewEntity ->
            if (viewEntity.status.isRunning()) {
                DFURunningProgressView(viewEntity.status, onEvent)
            } else if (viewEntity.status.isCompleted()) {
                DFUCompletedProgressView(viewEntity.status)
            } else {
                DFUIdleProgressView(onEvent)
            }
    }.exhaustive
}

@Composable
private fun DisabledDFUProgressView(viewEntity: ProgressItemViewEntity = ProgressItemViewEntity()) {
    DisabledCardComponent(
        titleIcon = R.drawable.ic_file_upload,
        title = stringResource(id = R.string.dfu_progress),
        description = stringResource(id = R.string.dfu_progress_idle),
        primaryButtonTitle = stringResource(id = R.string.dfu_progress_run),
    ) {
        ProgressItem(viewEntity)
    }
}

@Composable
private fun DFUCompletedProgressView(
    viewEntity: ProgressItemViewEntity = ProgressItemViewEntity()
) {
    CardComponent(
        titleIcon = R.drawable.ic_file_upload,
        title = stringResource(id = R.string.dfu_progress),
        description = stringResource(id = R.string.dfu_progress_running)
    ) {
        ProgressItem(viewEntity)
    }
}

@Composable
private fun DFURunningProgressView(
    viewEntity: ProgressItemViewEntity = ProgressItemViewEntity(),
    onEvent: (DFUViewEvent) -> Unit
) {
    CardComponent(
        titleIcon = R.drawable.ic_file_upload,
        title = stringResource(id = R.string.dfu_progress),
        description = stringResource(id = R.string.dfu_progress_running),
        primaryButtonTitle = stringResource(id = R.string.dfu_abort),
        primaryButtonAction = { onEvent(OnAbortButtonClick) }
    ) {
        ProgressItem(viewEntity)
    }
}

@Composable
private fun DFUIdleProgressView(
    onEvent: (DFUViewEvent) -> Unit
) {
    CardComponent(
        titleIcon = R.drawable.ic_file_upload,
        title = stringResource(id = R.string.dfu_progress),
        description = stringResource(id = R.string.dfu_progress_running),
        primaryButtonTitle = stringResource(id = R.string.dfu_progress_run),
        primaryButtonAction = { onEvent(OnInstallButtonClick) }
    ) {
        ProgressItem(ProgressItemViewEntity())
    }
}

@Composable
private fun ProgressItem(viewEntity: ProgressItemViewEntity) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ProgressItem(
            stringResource(id = R.string.dfu_progress_stage_bootloader),
            viewEntity.bootloaderStatus
        )
        Spacer(modifier = Modifier.size(8.dp))
        ProgressItem(stringResource(id = R.string.dfu_progress_stage_dfu), viewEntity.dfuStatus)
        Spacer(modifier = Modifier.size(8.dp))

        if (viewEntity.installationStatus == ProgressItemStatus.WORKING) {
            ProgressItem(
                stringResource(
                    id = R.string.dfu_display_status_progress_update,
                    viewEntity.progress
                ),
                viewEntity.installationStatus
            )
        } else {
            ProgressItem(
                stringResource(id = R.string.dfu_progress_stage_installing),
                viewEntity.installationStatus
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        if (viewEntity.resultStatus != ProgressItemStatus.ERROR) {
            ProgressItem(
                stringResource(id = R.string.dfu_progress_stage_completed),
                viewEntity.resultStatus
            )
        } else {
            ProgressItem(
                stringResource(
                    id = R.string.dfu_progress_stage_error,
                    viewEntity.errorMessage ?: stringResource(id = R.string.dfu_unknown)
                ),
                viewEntity.resultStatus
            )
        }
    }
}

@Composable
private fun ProgressItem(text: String, status: ProgressItemStatus) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(status.toImageRes()),
            contentDescription = stringResource(id = R.string.dfu_progress_icon),
            tint = status.toColor()
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = status.toColor()
        )
    }
}

@Composable
private fun ProgressItemStatus.toColor(): Color {
    return when (this) {
        ProgressItemStatus.DISABLED -> MaterialTheme.colorScheme.onSurfaceVariant
        ProgressItemStatus.WORKING -> Color.Unspecified
        ProgressItemStatus.SUCCESS -> colorResource(id = R.color.nordicGrass)
        ProgressItemStatus.ERROR -> MaterialTheme.colorScheme.error
    }
}

@DrawableRes
private fun ProgressItemStatus.toImageRes(): Int {
    return when (this) {
        ProgressItemStatus.DISABLED -> R.drawable.ic_remove
        ProgressItemStatus.WORKING -> R.drawable.ic_arrow_right
        ProgressItemStatus.SUCCESS -> R.drawable.ic_check
        ProgressItemStatus.ERROR -> R.drawable.ic_cross
    }
}
