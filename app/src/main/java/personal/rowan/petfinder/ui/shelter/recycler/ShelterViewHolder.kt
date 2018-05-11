package personal.rowan.petfinder.ui.shelter.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.shelter.ShelterListViewState
import rx.Subscription
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class ShelterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)  {

    val titleView: TextView by bindView(R.id.shelter_title)
    val subtitleView: TextView by bindView(R.id.shelter_subtitle)
    val subtextView: TextView by bindView(R.id.shelter_subtext)
    val petsButton: Button by bindView(R.id.shelter_pets_button)
    val directionsButton: Button by bindView(R.id.shelter_directions_button)

    private var mPetsButtonSubscription: Subscription? = null
    private var mDirectionsButtonSubscription: Subscription? = null

    fun bind(listViewState: ShelterListViewState, petsButtonSubject: PublishSubject<Pair<String?, String?>>, directionsButtonSubject: PublishSubject<String>) {
        titleView.text = listViewState.title()
        subtitleView.text = listViewState.subtitle()
        subtextView.setText(listViewState.subtext())

        if (mPetsButtonSubscription != null && !mPetsButtonSubscription!!.isUnsubscribed) {
            mPetsButtonSubscription!!.unsubscribe()
        }
        mPetsButtonSubscription = RxView.clicks(petsButton).subscribe { petsButtonSubject.onNext(Pair(listViewState.id(), listViewState.title())) }

        if (mDirectionsButtonSubscription != null && !mDirectionsButtonSubscription!!.isUnsubscribed) {
            mDirectionsButtonSubscription!!.unsubscribe()
        }
        mDirectionsButtonSubscription = RxView.clicks(directionsButton).subscribe { directionsButtonSubject.onNext(listViewState.address()) }
    }

}