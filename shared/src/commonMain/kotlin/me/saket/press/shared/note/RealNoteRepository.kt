package me.saket.press.shared.note

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.observable.Observable
import me.saket.press.data.shared.Note
import me.saket.press.data.shared.NoteQueries
import me.saket.press.shared.db.NoteId
import me.saket.press.shared.rx.Schedulers
import me.saket.press.shared.rx.asObservable
import me.saket.press.shared.rx.mapToList
import me.saket.press.shared.rx.mapToOneOrOptional
import me.saket.press.shared.time.Clock
import me.saket.press.shared.util.Optional

internal class RealNoteRepository(
  private val noteQueries: NoteQueries,
  private val schedulers: Schedulers,
  private val clock: Clock
) : NoteRepository {

  override fun note(id: NoteId): Observable<Optional<Note>> {
    return noteQueries.note(id)
        .asObservable(schedulers.io)
        .mapToOneOrOptional()
  }

  override fun visibleNotes(): Observable<List<Note>> {
    return noteQueries.visibleNotes()
        .asObservable(schedulers.io)
        .mapToList()
  }

  override fun create(vararg insertNotes: InsertNote): Completable {
    return completableFromFunction {
      noteQueries.transaction {
        for (note in insertNotes) {
          noteQueries.insert(
              id = note.id,
              content = note.content,
              createdAt = note.createdAt ?: clock.nowUtc(),
              updatedAt = note.createdAt ?: clock.nowUtc()
          )
        }
      }
    }
  }

  override fun update(id: NoteId, content: String): Completable {
    return completableFromFunction {
      noteQueries.updateContent(
          id = id,
          content = content,
          updatedAt = clock.nowUtc()
      )
    }
  }

  override fun markAsPendingDeletion(id: NoteId): Completable {
    return completableFromFunction {
      noteQueries.markAsPendingDeletion(id)
    }
  }

  override fun markAsArchived(id: NoteId): Completable {
    return completableFromFunction {
      noteQueries.setArchived(
          id = id,
          isArchived = true,
          updatedAt = clock.nowUtc()
      )
    }
  }
}
