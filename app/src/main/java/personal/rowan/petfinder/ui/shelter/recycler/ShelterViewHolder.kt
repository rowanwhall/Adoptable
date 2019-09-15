package personal.rowan.petfinder.ui.shelter.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.jakewharton.rxbinding.view.RxView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.shelter.ShelterListViewState
import rx.Subscription
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class ShelterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    private val titleView: TextView = itemView.findViewById(R.id.shelter_title)
    private val subtitleView: TextView = itemView.findViewById(R.id.shelter_subtitle)
    private val subtextView: TextView = itemView.findViewById(R.id.shelter_subtext)
    private val petsButton: Button = itemView.findViewById(R.id.shelter_pets_button)
    private val directionsButton: Button = itemView.findViewById(R.id.shelter_directions_button)

    private var mPetsButtonSubscription: Subscription? = null
    private var mDirectionsButtonSubscription: Subscription? = null

    fun bind(listViewState: ShelterListViewState, petsButtonSubject: PublishSubject<Pair<String?, String?>>, directionsButtonSubject: PublishSubject<String>) {
        titleView.text = listViewState.title()
        subtitleView.text = listViewState.subtitle()
        subtextView.text = listViewState.subtext()

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